package com.brownfield.pss.search.controller;

import com.brownfield.pss.search.component.SearchComponent;
import com.brownfield.pss.search.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchRestController {

    @Autowired
    SearchComponent searchComponent;

    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public List<Flight> search(@RequestBody SearchQuery searchQuery){
        return searchComponent.search(searchQuery);
    }
}
