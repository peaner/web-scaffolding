package web.scaffolding.core.page;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageWrapper<T> {

    public static final int MAX_PAGE_ITEM_DISPLAY = 7;

    public Page<T> page;

    public PageWrapper(Page<T> page) {
        this.page = page;
    }

    //可显示的页数列表
    public List<PageItem> getPageItems() {
        //当前页码
        int currentPage = page.getNumber();
        //显示的总页数
        int totalPages = page.getTotalPages();
        int startPage = 0;
        int pageItemSize = MAX_PAGE_ITEM_DISPLAY;

        if(totalPages <= MAX_PAGE_ITEM_DISPLAY) {
            //所有页数全部都显示出来
            pageItemSize = totalPages;
        } else {
            int halfDisplay = MAX_PAGE_ITEM_DISPLAY / 2;
            if(currentPage <= halfDisplay) {
                //起始页数
                //startPage = 0;
            } else if((totalPages - 1 - currentPage) <= halfDisplay) {
                //截止页数
                startPage = totalPages - MAX_PAGE_ITEM_DISPLAY;
            } else {
                startPage = currentPage - halfDisplay;
            }
        }

        List<PageItem> pageItems = Lists.newArrayList();
        for(int i = 0; i < pageItemSize; i ++) {
            int page = i + startPage;
            pageItems.add(new PageItem(page, page == currentPage));
        }

        return pageItems;
    }

    //是否有上一页
    public boolean isHasPrev() {
        return page.getNumber() > 0;
    }

    //是否有下一页
    public boolean isHasNext() {
        return page.getNumber() + 1 < page.getTotalPages();
    }

    //是否是第一页
    public boolean isFirst() {
        return !isHasPrev();
    }

    //是否是最后一页
    public boolean isLast() {
        return !isHasNext();
    }

    //每页显示的条目数
    public int getSize() {
        return page.getPageable().getPageSize();
    }

    //总条目数
    public long getTotalCount() {
        return page.getTotalElements();
    }

    //总页数
    public int getTotalPages() {
        return page.getTotalPages();
    }

    //当前页数
    public int getPage() {
        return page.getNumber();
    }

    public List<T> getContent() {
        return page.getContent();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class PageItem {

        private int number;

        private boolean current;
    }
}