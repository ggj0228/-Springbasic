package com.beyond.basic.b2_bold.repository;

import com.beyond.basic.b2_bold.domain.Author;
import com.beyond.basic.b2_bold.dto.AuthorDetailDto;
import com.beyond.basic.b2_bold.dto.AuthorListDto;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;

    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

    public List<Author> findAll() {
        return this.authorList;
    }

    public Optional<Author> findById(Long id) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getId().equals(id)) {
                author = a;
            }
        }
        return  Optional.ofNullable(author);
    }


    public Optional<Author> findByEmail(String email) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getEmail().equals(email)) {
                author = a;
            }
        }
        return  Optional.ofNullable(author);
    }
    public void delete(Long id) {
        //id값으로 요소의 index를 찾아서 제거
        for (int i = 0; i < this.authorList.size(); i++) {
            if (this.authorList.get(i).getId().equals(id)) {
                this.authorList.remove(i);
                break;
            }
        }
    }



}
