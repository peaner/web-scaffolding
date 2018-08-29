package web.scaffolding.core.page;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: lilongzhou
 * @Description:
 * @Date: Created in 19:06 2018/8/14
 */
@Setter
@Getter
public class PageBean<T> {

    public static final int MAX_PAGE_ITEM_DISPLAY = 7;

    private int number;

    // 总记录数
    private Long totalCount;

    // 总页数
    private int totalPages;

    // 每页显示的条目数
    private int size;

    //可显示的页数列表
    public List<PageItem> getPageItems() {
        //当前页码
        int currentPage = this.number;
        //显示的总页数
        int totalPages = this.totalPages;
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

    // 每页记录数
    private List<T> content;


    @Getter
    @Setter
    @AllArgsConstructor
    public class PageItem {

        private int number;

        private boolean current;
    }


}
