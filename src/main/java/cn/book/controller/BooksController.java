package cn.book.controller;

import cn.book.pojo.Bookadmin;
import cn.book.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Book;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/a")
public class BooksController {

    @Autowired
    BooksService booksService;

//    //    设置首页
//    @RequestMapping("/")
//    public String index(){
//        return "index";
//    }

    //列出数据表格
    //    设置listBooks页面(list第一种写法)
    @RequestMapping("/listBooks")
    public ModelAndView listBooks(){
        ModelAndView mav = new ModelAndView();
        List<Bookadmin> bb = booksService.list();
        mav.addObject("bb",bb);
        mav.setViewName("listBooks");
        return mav;
    }
    //    list的第二种写法
//    @RequestMapping("/listBooks")
//    public String listBooks(Model model){
//        List<Bookadmin> bb = booksService.list();
//        model.addAttribute("bb",bb);
//        return "listBooks";
//    }

    //    添加数据（两部分）
    //    第一步：跳转到这里并添加图书信息，点击添加按钮就执行下边第二段代码
    @RequestMapping("/addBooks0")
    public String addBooks0(){
        return "savepage";
    }
    //    第二步：把下边的页面数据返回给后端，再跳转到listBooks页面
    @RequestMapping(value = "/addBooks",method = RequestMethod.POST)
    public String addBooks(Bookadmin bookadmin, MultipartFile file,Model model) {
            booksService.insertBook(bookadmin, file);
            model.addAttribute("bookadmin",bookadmin);
            return ("redirect:listBooks");
    }

    //    修改数据（两部分）
    //    第一步：更新图书，先通过bid找到图书，并列在/updatepage/{bid}页面上，
    @RequestMapping("/updatepage/{bid}")
    public String updatepage(@PathVariable("bid") int bid,Model model){
        model.addAttribute("bookadmin",booksService.getBookByBid(bid));
        return "updatepage";
    }
    //    第二步：然后修改即可，在这里点更新提交数据给后端
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update(Bookadmin b, MultipartFile file){
        booksService.update(b,file);
        return "redirect:listBooks";
    }

    //    删除图书数据
    @RequestMapping("/deleteBooksByBid")
    public String deleteBooksByBid(Bookadmin bid){
        booksService.deleteBookByBid(bid);
        return "redirect:listBooks";
    }

    //列出单个图书的详情
    @RequestMapping("/bookDetail/{bid}")
    public String detailByBid(@PathVariable("bid") int bid,Model model){
        model.addAttribute("bookDetail",booksService.getBookByBid(bid));
        return "bookDetail";
    }

    //添加到购物车，利用session存储在服务器
    @RequestMapping("/addToCart")
    public String addToCart(Bookadmin bookadmin, HttpServletRequest request,Model model){
        Map<Bookadmin,Integer> bookCart = (Map<Bookadmin,Integer>)request.getSession(false).getAttribute("useradmin");
        if (bookCart == null){
            bookCart = new HashMap<>();
            bookadmin = booksService.getBookByBid(bookadmin.getBid());
            bookCart.put(bookadmin,1);
            request.getSession(false).setAttribute("useradmin",bookCart);
        }else {
            Integer alreadyInBookCart = bookCart.get(bookadmin);
            if (alreadyInBookCart == null){
                bookadmin = booksService.getBookByBid(bookadmin.getBid());
                bookCart.put(bookadmin,1);
            }else {
                bookCart.put(bookadmin,1+alreadyInBookCart);
            }
        }
        BigDecimal sum = new BigDecimal(0);
        for (Bookadmin bc : bookCart.keySet()){
            sum = sum.add(bc.getPrice().multiply(new BigDecimal(bookCart.get(bc))));
        }
        model.addAttribute("sum",sum);
        return "bookCart";
    }

//    //添加到购物车，利用session存储在服务器
//    @RequestMapping("/addToCart")
//    public String addToCart(Bookadmin bookadmin, HttpServletRequest request,Model model){
//        Map<Bookadmin,Integer> bookCart = (Map<Bookadmin,Integer>)request.getSession(false).getAttribute("bookCart");
//        if (bookCart == null){
//            bookCart = new HashMap<>();
//            bookadmin = booksService.getBookByBid(bookadmin.getBid());
//            bookCart.put(bookadmin,1);
//            request.getSession(true).setAttribute("bookCart",bookCart);
//        }else {
//            Integer alreadyInBookCart = bookCart.get(bookadmin);
//            if (alreadyInBookCart == null){
//                bookadmin = booksService.getBookByBid(bookadmin.getBid());
//                bookCart.put(bookadmin,1);
//            }else {
//                bookCart.put(bookadmin,1+alreadyInBookCart);
//            }
//        }
//        BigDecimal sum = new BigDecimal(0);
//        for (Bookadmin bc : bookCart.keySet()){
//            sum = sum.add(bc.getPrice().multiply(new BigDecimal(bookCart.get(bc))));
//        }
//        model.addAttribute("sum",sum);
//        return "bookCart";
//    }

//    <<<<<<<<<<<<<<<<<<<<<<<<<<<垃圾
//    先跳转到这个页面，然后修改信息，点击提交，再使用上边的代码提交到后端
//    @RequestMapping("/updateBooks0")
//    public String updateBooks0(@RequestParam(value = "bid") Bookadmin bid,Model model){
//        UpdateBooks0 updateBooks0 = booksService.getBookByBid(bid);
//        model.addAttribute("bookadmin",);
//        return "updatepage";
//    }
//    @RequestMapping(value = "/updatepage/{bid}")
//    public String updatepage(@PathVariable int bid,Model m){
//        Bookadmin bookadmin = booksService.getBookByBid(bid);
//        m.addAttribute("bi",bid);
//        return "updatepage";
//    }
//    //    更新图书信息，返回给listBooks页面（图书列表页面）
//    @RequestMapping(value = "/updateBooks",method = RequestMethod.POST)
//    public String update(Bookadmin b){
//        booksService.update(b);
//        return "redirect:listBooks";
//    }
//    垃圾>>>>>>>>>>>>>>>>>>>>>>>>>>
}
