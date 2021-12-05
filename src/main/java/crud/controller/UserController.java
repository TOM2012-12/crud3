package crud.controller;

import crud.bean.User;
import crud.service.UserService;
import crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import crud.utils.JwtUtil;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping("/selectUser")
    @ResponseBody
    public String selectUser(@RequestParam("userid")String userid, @RequestParam("password") String password,HttpServletResponse response) {
        User user = userService.selectUser(userid, password);
        //System.out.println(user.getPassword());
        if (user != null) {
            String token = JwtUtil.sign(userid, password);
            if (token != null) {
                System.out.println(token);
                Cookie cookie = new Cookie("user", token);
                cookie.setMaxAge(3600);//设置token有效时间
                //model.addAttribute("user", token);
                response.addCookie(cookie);
                return response.toString();
            }
        } else{
            return "error";}
        return null;

    }
@RequestMapping("/checkUser")
@ResponseBody
    public String checkUser(@RequestParam("userid") String userid){
        boolean check=userService.CheckUserByid(userid);
        if(check)
        return "success";
        else
            return "error";

}
@RequestMapping("/adddUser")
@ResponseBody
    public String addUser(User user){
        boolean check=userService.AddUser(user);
    if(check)
        return "success";
    else
        return "error";
}
    @ResponseBody
    @RequestMapping(value = "/token_submit")
    public String BackToken(User user){
        //先根据用户来查询是否存在，存在就返回token不存在就返回错误信息
        User user1=userService.selectUser(user.getUserid(),user.getPassword());
        String check=null;
        if(user1==null){
            check="error";
        }
           else{
               check=JwtUtil.sign(user.getUserid(),user.getPassword());
        }



        return check;
    }


    @ResponseBody
    @RequestMapping(value="/tokencheck")
    public String checkToken(String token,HttpServletResponse response){
        System.out.println(token);
        boolean check=JwtUtil.verify(token);
        String BackCheckValue=null;
        System.out.println(check);
        if(check==false){
            BackCheckValue="error";
        }else{
            Cookie cookie = new Cookie("user", token);
            cookie.setMaxAge(3600);//设置token有效时间
            //model.addAttribute("user", token);
            response.addCookie(cookie);
            BackCheckValue="true";

        }
        return BackCheckValue;
    }
}
