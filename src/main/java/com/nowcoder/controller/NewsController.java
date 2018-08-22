package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JrtoutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;


    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){

        News news = newsService.getById(newsId);
        if (news != null){
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            } else {
                model.addAttribute("like", 0);
            }
            //评论
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            //放入ViewObject中
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for (Comment comment : comments) {
                ViewObject commentVO = new ViewObject();
                commentVO.set("comment", comment);
                commentVO.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(commentVO);
            }
            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        return "detail";
    }

    //添加评论
    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId, @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);
            //更新news中的评论数量
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
            //怎么异步化
        }catch (Exception e){
            logger.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    //图片显示到前端（图片下载）
    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(JrtoutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误"+imageName+e.getMessage());
        }

    }
    @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image, @RequestParam("title") String title,
                          @RequestParam("link") String link){

        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
            news.setUserId(hostHolder.getUser().getId());
            }else {
                //匿名ID
                news.setUserId(3);
            }
            newsService.addNews(news);
            return JrtoutiaoUtil.getJSONString(0);

        }catch (Exception e){
            logger.error("资讯添加错误"+e.getMessage());
            return JrtoutiaoUtil.getJSONString(1,"发布失败");
        }
    }


    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try{
//            String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null){
                return JrtoutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return JrtoutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return JrtoutiaoUtil.getJSONString(1,"上传失败");
        }
    }
}
