package com.wang.service.impl;

import com.wang.annotation.AddCache;
import com.wang.annotation.AddLog;
import com.wang.annotation.DelCache;
import com.wang.dao.VideoDao;
import com.wang.entity.yx_Category;
import com.wang.entity.yx_Video;
import com.wang.po.VideoPo;
import com.wang.repository.VideoRepository;
import com.wang.service.VideoService;
import com.wang.util.AliyunOSSUtil;
/*import com.wang.util.InterceptVideoCoverUtil;*/

import com.wang.util.InterceptVideoCoverUtil;
import com.wang.util.UUIDUtil;
import com.wang.vo.VideoVo;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: Video_ServiceImpl
 * @BelongsProject: yingx
 * @BelongsPackage: com.wang.service.impl
 * @Author:wang
 * @Date: 2020/8/27——23:15
 * @Description: TOOO
 */
@Service("VideoService")
@Transactional()

public class Video_ServiceImpl implements VideoService {
    @Resource
    VideoDao videoDao;

    @Resource
    HttpSession session;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    @AddLog(value = "查询视频信息")
    @AddCache
    public HashMap<String, Object> queryAllPage(Integer page, Integer rows) {

        HashMap<String, Object> map = new HashMap<>();

        //当前页   page
        map.put("page", page);

        //总条数   records
        Example e = new Example(yx_Video.class);//创建类型对象
        int records = videoDao.selectCountByExample(e);
        map.put("records", records);

        //总页数   total
        //总页数=总条数/每页展示条数   有余数加一页
        Integer total = records / rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);

        //数据    rows   参数：略过几条，要几条
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<yx_Video> videos = videoDao.selectByRowBounds(new yx_Video(), rowBounds);
        map.put("rows", videos);

        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    @AddLog(value = "添加视频信息到本地数据库")

    public String add(yx_Video video) {

        video.setId(UUIDUtil.getUUID());
        video.setPublishiDate(new Date());

        //执行添加
        videoDao.insertSelective(video);

        //将id返回
        return video.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    @AddLog(value = "添加视频信息到阿里云服务器")
    @DelCache
    public void uploadVoidAliyun(String id, MultipartFile path, HttpServletRequest request) {
        //获取视频上传文件名  加上时间戳
        String filename = System.currentTimeMillis() + "_" + path.getOriginalFilename();
        System.out.println("视频文件名:" + filename);
        //进行远程文件上传  需要用到阿里云工具 byte上传
        AliyunOSSUtil.uploadFileByte(path, "yingx-wangs", "video/" + filename);//MultipartFile类型的文件    存储空间名(Bucket名称)    文件名
        //获取远程阿里云视频存放路径  https://Bucket名称.oss-cn-beijing.aliyuncs.com/文件名/
        String fetfilePath = "https://yingx-wangs.oss-cn-beijing.aliyuncs.com/video/" + filename;
        //因为截取视频封面 需要 //远程路径 和 存储本地图片路径
        //指定本地图片存储路径  （获取绝对路径）
        String realPath = request.getSession().getServletContext().getRealPath("/bootstrap/img");
        //判断文件夹是否存在
        File file = new File(realPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();//创建文件夹
            System.out.println("本地文件是否存在" + mkdirs);
        }
        //获取图片封面文件名  例 视频文件名1598364600878_5f3f6c9b770e1.mp4
        String[] split = filename.split("\\.");  //根据.进行字符串拆分  拆成了 1598364600878_5f3f6c9b770e1  和 mp4
        //设置图片文件名
        String imgname = split[0] + ".jpg";
        System.out.println("封面文件名：" + imgname);
        //设置图片存储位置
        String imgPath = realPath + "\\" + imgname;
        System.out.println("封面存储本地位置：" + imgPath);
        //进行视频封面截取  然后将截取到的封面进行本地上传阿里云
        try {
            InterceptVideoCoverUtil.fetchFrame(fetfilePath, imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //将本地视频封面上传 到阿里云
        AliyunOSSUtil.uploadLocalFiles("yingx-wangs", "photo/" + imgname, imgPath);

        //删除本地封面文件
        File file1 = new File(imgPath);
        if (file1.isFile() && file1.exists()) {
            boolean delete = file1.delete();
            System.out.println("删除封面文件:" + delete);
        }


        //修改数据库视频数据
        yx_Video video = new yx_Video();
        video.setId(id);
        //视频地址
        video.setPath(fetfilePath);
        //视频封面
        video.setCover("https://yingx-wangs.oss-cn-beijing.aliyuncs.com/photo/" + imgname);
        videoDao.updateByPrimaryKeySelective(video); //通过主键进行修改  没有添加的属性 对数据库不操作


            //查询数据  修改完数据后  查询此消息 然后添加到索引
            yx_Video video1 = videoDao.selectByPrimaryKey(id);
            //添加索引
            videoRepository.save(video1);

    }

    //删除视频
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AddLog(value = "删除视频信息")
    @DelCache
    public HashMap<String, Object> delete(yx_Video video) {
        HashMap<String, Object> map = new HashMap<>();
        //一下一个环节出错就停止操作
        try {
            //根据id查询视频
            yx_Video yx_video = videoDao.selectByPrimaryKey(video);
            //获取视频地址
            String path = yx_video.getPath();
            //获取视频封面地址
            String cover = yx_video.getCover();
            //分别拆分 视频地址  和视频封面地址
            // https://yingx-wangs.oss-cn-beijing.aliyuncs.com/video/1598689789502_wx_camera_1581165202257.mp4
            //beijing.aliyuncs.com/photo/1598689858865_wx_camera_1581165202257.jpg
            String[] split = path.split("/");//视频地址  拆分了 五段（其中//中有一段） https:  “ ”  yingx-wangs.oss-cn-beijing.aliyuncs.com    video    1598689789502_wx_camera_1581165202257.mp4
            String[] split1 = cover.split("/");//视频封面地址 与视频拆分一样 五段
            //获取视频文件名
            String videopath = split[split.length - 2] + "/" + split[split.length - 1]; //video+1598689789502_wx_camera_1581165202257.mp4
            System.out.println("视频地址为：" + videopath);
            //获取视频封面文件名
            String imagepath = split1[split1.length - 2] + "/" + split1[split1.length - 1]; //video+1598689789502_wx_camera_1581165202257.mp4
            System.out.println("视频地址为：" + imagepath);
            //删除阿里云视频
            AliyunOSSUtil.deleteFile("yingx-wangs", videopath);//存储空间名  文件名
            //删除阿里云视频封面
            AliyunOSSUtil.deleteFile("yingx-wangs", imagepath);
            //删除数据库视频信息
            videoDao.deleteByPrimaryKey(video);
            //删除索引
            videoRepository.delete(video);
            map.put("message", "删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "删除失败!");
        }

        return map;
    }

    //更改视频基本信息
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @AddLog(value = "更改视频信息")
    @DelCache
    public void update(yx_Video video) {
        yx_Video yx_video = videoDao.selectByPrimaryKey(video);
        System.out.println("更改视频基本信息"+yx_video);
        video.setPath(yx_video.getPath());
        video.setCover(yx_video.getCover());
        videoDao.updateByPrimaryKeySelective(video);

        yx_Video video1 = videoDao.selectByPrimaryKey(video.getId());
        //修改索引
        videoRepository.save(video1);
        System.out.println("更改视频基本信息222"+video1);
    }


    //前端首页视频数据查询
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    //查询po对象 然后对其vo对象进行赋值
    public List<VideoVo> queryFirst() {
        //创建list集合用于存储vo对象数据
        List<VideoVo> list = new ArrayList<>();
        //赋值转换为vo对象是如有错误则停止并且返回错误
        try {
            //转换日期格式
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            //查询vo数据
            List<VideoPo> videoPos = videoDao.queryByReleaseTime();
            //将vo对象睡觉的值赋值给po对象
            for(VideoPo p:videoPos){
                String time=sdf.format(p.getPublishDate());//获取时间进行格式设置
                /*
                * 将po数据赋值给vo对象数据 因为点赞是在redis中查询所以暂时 用0代替
                * */
                VideoVo v=new VideoVo(p.getId(),p.getTitle(),p.getCover(),p.getPath(),time,p.getBrief(),0,p.getCateName(),p.getHeadImg());
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }

    @Resource
    ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    VideoRepository videoRepository;

    //索引查询
    @Override
    public List<yx_Video> searchVideo(String content) {

        //设置查询条件
        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                .withIndices("yingx")  //指定索引
                .withTypes("video")  //指定类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("title").field("brief")) //指定查询条件
                .build();

        List<yx_Video> videos = elasticsearchTemplate.queryForList(searchQuery, yx_Video.class);
        return videos;
    }
    //索引高亮查询
    @Override
    public List<yx_Video> searchVideos(String content) {

        //处理高亮
        HighlightBuilder.Field field = new HighlightBuilder.Field("*");
        field.preTags("<span style='color:red'>"); //前缀
        field.postTags("</span>");  //后缀
        field.requireFieldMatch(false); //开启多行高亮

        //设置查询条件
        SearchQuery searchQuery=new NativeSearchQueryBuilder()
                .withIndices("yingx")  //指定索引
                .withTypes("video")  //指定类型
                .withQuery(QueryBuilders.queryStringQuery(content).field("biref")) //指定查询条件
                .withHighlightFields(field) //指定高亮
                .build();

        AggregatedPage<yx_Video> videos = elasticsearchTemplate.queryForPage(searchQuery, yx_Video.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {

                ArrayList<yx_Video> videos = new ArrayList<>();

                //获取数据数组
                SearchHit[] hits = searchResponse.getHits().getHits();
                //遍历数组
                for (SearchHit hit : hits) {

                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                    String id = hit.getSourceAsMap().get("id")!=null?hit.getSourceAsMap().get("id").toString():null;
                    String title = sourceAsMap.get("title")!=null?hit.getSourceAsMap().get("title").toString():null;
                    String brief = sourceAsMap.get("biref")!=null?hit.getSourceAsMap().get("biref").toString():null;
                    String path = sourceAsMap.get("path")!=null?hit.getSourceAsMap().get("path").toString():null;
                    String cover = sourceAsMap.get("cover")!=null?hit.getSourceAsMap().get("cover").toString():null;
                    String userId = sourceAsMap.get("userID")!=null?hit.getSourceAsMap().get("userID").toString():null;
                    String categoryId = sourceAsMap.get("categoryId")!=null?hit.getSourceAsMap().get("categoryId").toString():null;
                    String groupId = sourceAsMap.get("groupID")!=null?hit.getSourceAsMap().get("groupID").toString():null;

                    Date publishDate = null;
                    //处理日期  获取日期
                    if(sourceAsMap.get("publishiDate")!=null){
                        String publishDateStr = hit.getSourceAsMap().get("publishiDate").toString();

                        //设置格式
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            //日期转换
                            publishDate = format.parse(publishDateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    //封装高亮对象
                    yx_Video video = new yx_Video(id, title, brief, path, cover, publishDate, categoryId, groupId, userId);

                    //获取高亮的结果
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                    //判断高亮的结果不为空
                    if (highlightFields.get("title") != null) {
                        //取出高亮结果
                        String titles = highlightFields.get("title").fragments()[0].toString();
                        video.setTitle(titles);  //将高亮结果放入对象
                    }
                    if (highlightFields.get("biref") != null) {
                        //取出高亮结果
                        String briefs = highlightFields.get("biref").fragments()[0].toString();
                        video.setBiref(briefs);
                    }
                    //将对象放入集合
                    videos.add(video);
                }
                //强转返回
                return new AggregatedPageImpl<T>((List<T>) videos);
            }
        });

        //获取返回结果
        List<yx_Video> videoList = videos.getContent();

        return videoList;
    }




/* @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void uploadVdieos(MultipartFile headImg, String id, HttpServletRequest request) {

        //1.获取文件上传的路径  根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/video");

        //2.判断文件夹是否存在
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();  //创建文件夹
        }
        //获取文件名
        String filename = headImg.getOriginalFilename();

        //创建一个新的名字    原名称-时间戳  10.jpg-1590390153130
        String newName = System.currentTimeMillis() + "-" + filename;

        //3.文件上传
        try {
            headImg.transferTo(new File(realPath, newName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4.修改图片路径
        //修改的条件

        Example e = new Example(yx_Video.class);//创建类型对象
        e.createCriteria().andEqualTo("id", id);

        yx_Video video = new yx_Video();
        video.setCover(""); //设置封面
        video.setPath(newName); //设置视频地址

        //修改
        videoDao.updateByExampleSelective(video, e);
    }

    *//*
     * 1.上传视频至阿里云
     * 2.截取视频的第一帧作为封面
     * 3.将封面存储到本地
     * 4.上传本地封面
     * 5.删除本地封面
     * 6.修改数据
     * *//*
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void uploadVdieosAliyun(MultipartFile path, String id, HttpServletRequest request) {


        //1。上传至阿里云
        //1.1.获取文件名
        String filename = path.getOriginalFilename();
        String newName = System.currentTimeMillis() + "-" + filename;  //1594000325773-树叶.mp4
        //1.2设置目录
        String menuName = "video/" + newName;

        *//*1.3.上传文件至阿里云
     * 参数：
     *   headImg:MultipartFile类型的文件
     *   bucketName：存储空间名
     *   fileName：文件名
     * *//*
        AliyunOSSUtil.uploadFileByte(path, "yingx-192", menuName);

        //2.截取视频封面

        //2.1.拼接视频路径
        String videoPath = "https://yingx-192.oss-cn-beijing.aliyuncs.com/" + menuName;

        //2.2.获取文件上传的路径  根据相对路径获取绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/cover");

        //2.3.判断文件夹是否存在
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();  //创建文件夹
        }

        //2.4.拆分字符串
        //newName=1594000325773-树叶.mp4
        String[] split = newName.split("\\.");
        String coverName = split[0];
        System.out.println("===coverName: " + coverName);

        //2.5.拼接封面名
        String coverNames = coverName + ".jpg";
        System.out.println("===coverNames: " + coverNames);

        //"D:\\biudata\\vedio\\test5.jpg"
        //2.6.拼接本地路径
        String localPath = realPath + "\\" + coverNames;
        System.out.println("===localPath:" + localPath);

        *//**2.7.截取视频封面
     * 获取指定视频的帧并保存为图片至指定目录
     * @param videofile  源视频文件路径   阿里云
     * @param localfile  截取帧的图片存放路径
     * @throws Exception
     *//*
        try {
            //  InterceptVideoCoverUtil.fetchFrame(videoPath,localPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        *//*3.上传视频封面至阿里云
     * 上传本地文件(封面)
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     *   localFile: 本地文件路径
     * *//*
        AliyunOSSUtil.uploadLocalFiles("yingx-192", "cover/" + coverNames, localPath);

        //4.删除本地文件
        //1.获取文件上传的路径  根据相对路径获取绝对路径
        File files = new File(localPath);

        //判断是一个文件，并且文件存在
        if (files.exists() && files.isFile()) {
            //删除文件
            boolean isDel = files.delete();
            System.out.println("删除：" + isDel);
        }

        //四，修改后台数据
        //修改的条件
        Example e = new Example(yx_Video.class);//创建类型对象
        e.createCriteria().andEqualTo("id", id);

        yx_Video video = new yx_Video();


        video.setCover("https://yingx-192.oss-cn-beijing.aliyuncs.com/cover/" + coverNames); //设置封面
        video.setPath(videoPath); //设置视频地址

        //修改
        videoDao.updateByExampleSelective(video, e);

    }

    *//*
     * 1.上传视频至阿里云
     * 2.截取视频的第一帧作为封面
     * 3.上传本地封面
     * 4.修改数据
     * *//*
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void uploadVdieosAliyuns(MultipartFile path, String id, HttpServletRequest request) {
        //1。上传至阿里云
        //1.1.获取文件名
        String filename = path.getOriginalFilename();
        String newName = System.currentTimeMillis() + "-" + filename;  //1594000325773-树叶.mp4
        //1.2设置目录
        String menuName = "video/" + newName;

        *//*1.3.上传文件至阿里云
     * 参数：
     *   headImg:MultipartFile类型的文件
     *   bucketName：存储空间名
     *   fileName：文件名
     * *//*
        AliyunOSSUtil.uploadFileByte(path, "yingx-192", menuName);

        //2.截取视频封面

        //2.1.拆分字符串
        //newName=1594000325773-树叶.mp4
        String[] split = newName.split("\\.");
        String coverName = split[0];

        //2.2.拼接封面名
        String coverNames = coverName + ".jpg";

        *//*2.3.截取视频封面
     * 视频截取帧
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     * *//*
        URL url = AliyunOSSUtil.videoInterceptCover("yingx-192", menuName);

        *//*
     * 3.上传视频封面至阿里云
     * 参数：
     *   netPath:网络路径
     *   bucketName：存储空间名
     *   fileName：指定文件名
     * *//*
        try {
            AliyunOSSUtil.uploadNetFile(url.toString(), "yingx-192", "cover/" + coverNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //四，修改后台数据
        //修改的条件

        Example e = new Example(yx_Video.class);//创建类型对象
        e.createCriteria().andEqualTo("id", id);

        yx_Video video = new yx_Video();


        video.setCover("https://yingx-192.oss-cn-beijing.aliyuncs.com/cover/" + coverNames); //设置封面
        video.setPath("https://yingx-192.oss-cn-beijing.aliyuncs.com/video/" + newName); //设置视频地址

        //修改
        videoDao.updateByExampleSelective(video, e);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(yx_Video video) {

        if (video.getPath() == "") {
            video.setPath(null);
        }
        System.out.println("修改：" + video);
        videoDao.updateByPrimaryKeySelective(video);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public HashMap<String, Object> delete(yx_Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {

            //设置条件

            Example e = new Example(yx_Video.class);//创建类型对象
            e.createCriteria().andEqualTo("id", video.getId());


            //根据id查询视频数据
            yx_Video videos = videoDao.selectOneByExample(e);
            //1.删除数据
            videoDao.deleteByExample(e);

            //2.删除本地文件
            //1.获取文件上传的路径  根据相对路径获取绝对路径
            //根据相对路径获取绝对路径
            String realPath = session.getServletContext().getRealPath("/upload/video");
            File files = new File(realPath + "/" + videos.getPath());

            //判断是一个文件，并且文件存在
            if (files.exists() && files.isFile()) {
                //删除文件
                boolean isDel = files.delete();
                System.out.println("删除：" + isDel);
            }

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public HashMap<String, Object> deleteAliyun(yx_Video video) {
        HashMap<String, Object> map = new HashMap<>();
        try {

            //设置条件

            Example e = new Example(yx_Video.class);//创建类型对象
            e.createCriteria().andEqualTo("id", video.getId());


            //根据id查询视频数据
            yx_Video videos = videoDao.selectOneByExample(e);

            //1.删除数据
            videoDao.deleteByExample(e);

            //获取视频和封面路径
            String path = videos.getPath();
            String cover = videos.getCover();

            //截取视频封面名称
            String videoName = path.replace("https://yingx-192.oss-cn-beijing.aliyuncs.com/", "");
            String coverName = cover.replace("https://yingx-192.oss-cn-beijing.aliyuncs.com/", "");
            System.out.println("--videoName: " + videoName);
            System.out.println("--coverName: " + coverName);

            *//*
     * 2.删除视频
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     * *//*
            AliyunOSSUtil.deleteFile("yingx-192", videoName);

            //2.删除封面
            AliyunOSSUtil.deleteFile("yingx-192", coverName);

            map.put("status", "200");
            map.put("message", "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "400");
            map.put("message", "删除失败");
        }
        return map;
    }*/
}
