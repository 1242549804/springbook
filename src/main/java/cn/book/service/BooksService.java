package cn.book.service;

import cn.book.pojo.Bookadmin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BooksService {

    List<Bookadmin> list();

    int insertBook(Bookadmin bookadmin);

    int update(Bookadmin b);

    int deleteBookByBid(Bookadmin bid);

    Bookadmin getBookByBid(int bid);
//
//    int searchBookByBn(String bn);
}
