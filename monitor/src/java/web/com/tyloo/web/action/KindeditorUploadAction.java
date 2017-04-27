package com.tyloo.web.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.tyloo.fw.waf.WebContext;
import com.tyloo.web.ImgYsuo;
import com.tyloo.web.WebAction;
import com.tyloo.web.WebHelper;

public class KindeditorUploadAction extends WebAction {

    @Override
    public String webProcess(WebContext context, Object member)
            throws ServletException
    {
        context.put("base", context.getRequest().getContextPath());
        WebHelper.putCommonValues(context);
        context.put("action", "KindeditorUploadAction");
        
        //文件保存目录路径
        
        String savePath = servlet.getServletContext().getRealPath("/") + "/attached/";
        
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        
        //文件保存目录URL
        String prefixString = context.getParameter("prefix");
        String saveUrl = "";
        if(StringUtils.isEmpty(prefixString))
        {
        	 saveUrl  = "/attached/";
        }
        else
        {        	
             saveUrl  = request.getContextPath() + "/attached/";
        }
       
        
        //定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("excel", "xls");
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
        extMap.put("all", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmv,swf,flv,gif,jpg,jpeg,png,bmp");

        //最大文件大小
        long maxSize = 20000000;

        response.setContentType("text/html; charset=UTF-8");
        try
        {
            if(!ServletFileUpload.isMultipartContent(request)){
            	response.getWriter().println(getError("文件不能为空。"));//文件不能为空
                return null;
            }
            //检查目录
            File uploadDir = new File(savePath);
            if(!uploadDir.exists())
            {
            	uploadDir.mkdirs();
            }
            
            if(!uploadDir.isDirectory()){
            	response.getWriter().println(getError("上传目录不存在。"));
                return null;
            }
            //检查目录写权限
            if(!uploadDir.canWrite()){
            	response.getWriter().println(getError("上传目录没有写权限。"));
                return null;
            }

            String dirName = request.getParameter("dir");
            if (dirName == null) {
                dirName = "image";
            }
            if(!extMap.containsKey(dirName)){
            	response.getWriter().println(getError("目录名不正确。"));
                return null;
            }
            //创建文件夹
            savePath += dirName + "/";
            saveUrl += dirName + "/";
            File saveDirFile = new File(savePath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String ymd = sdf.format(new Date());
            savePath += ymd + "/";
            saveUrl += ymd + "/";
            File dirFile = new File(savePath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            List items = upload.parseRequest(request);
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                String fileName = item.getName();
                if (!item.isFormField()) {
                    //检查文件大小
                    if(item.getSize() > maxSize){
                    	response.getWriter().println(getError("上传文件大小超过限制。"));
                        return null;
                    }
                    //检查扩展名
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
                    	response.getWriter().println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
                        return null;
                    }

                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    int randomNum = new Random().nextInt(1000);
                    String newFileName = df.format(new Date()) + "_" + randomNum + "." + fileExt;
                    String newFileNameMini = df.format(new Date()) + "_" + randomNum + "_mini." + fileExt;
                    try{
                        File uploadedFile = new File(savePath, newFileName);
                        item.write(uploadedFile);
                        
                        if("image".equals(dirName))
                        {
                        	 //生成小图片
                            ImgYsuo imgCom = new ImgYsuo(savePath+"/"+newFileName);
                            imgCom.resizeFix(350, 350, savePath+"/"+newFileNameMini);
                        }
                       
                        
                    }catch(Exception e){
                    	response.getWriter().println(getError("上传文件失败。"));
                        return null;
                    }

                    JSONObject obj = new JSONObject();
                    obj.put("error", 0);
                    obj.put("url", saveUrl + newFileName);
                    System.out.println(obj.toJSONString());
                    response.getWriter().println(obj.toJSONString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
    
    private String getError(String message) {
        JSONObject obj = new JSONObject();
        obj.put("error", 1);
        obj.put("message", message);
        return obj.toJSONString();
    }
    
}
