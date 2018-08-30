package web.scaffolding.core.page;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class PageUtils<T> {

    public static final int MAX_PAGE_ITEM_DISPLAY = 7;

    private List<PageItem> sliders = new ArrayList<PageItem>();

    private List<T> items;

    private long page;

    private long totalCount;

    private long sliderCount;

    private int pageSize;

    private long totalPages;

    public PageUtils(Pageable pageable, List<T> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
        this.pageSize = pageable.getPageSize();

        page = pageable.getPageNumber() + 1; //start from 1 to match page.page

        long start;
        if (getTotalPages() <= MAX_PAGE_ITEM_DISPLAY) {
            start = 1;
            sliderCount = getTotalPages();
        } else {
            if (page <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY / 2) {
                sliderCount = MAX_PAGE_ITEM_DISPLAY;
                start = 1;
            } else if (page >= getTotalPages() - MAX_PAGE_ITEM_DISPLAY / 2) {
                sliderCount = MAX_PAGE_ITEM_DISPLAY;
                start = getTotalPages() - sliderCount + 1;
            } else {
                sliderCount = MAX_PAGE_ITEM_DISPLAY;
                start = page - sliderCount / 2;
            }
        }

        for (int i = 0; i < sliderCount; i++) {
            sliders.add(new PageItem((start + i), (start + i) == page));
        }
    }

    public List<PageItem> getSliders() {
        return sliders;
    }

    public long getPage() {
        return page;
    }

    public List<T> getItems() {
        return items;
    }

    public long getSliderCount() {
        return sliderCount;
    }

    public long getTotalPages() {
        if (totalPages == 0) {
            totalPages = (long) Math.ceil((double) totalCount / (double) (pageSize));
        }
        return totalPages;
    }

    public boolean isFirst() {
        return this.page == 1;
    }

    public boolean isLast() {
        return this.page == getTotalPages() || getTotalPages() == 0;
    }

    public boolean isHasPrevious() {
        return page > 1;
    }

    public boolean isHasNext() {
        return page < getTotalPages();
    }

    public long getTotalCount() {
        return totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public class PageItem {

        private long number;

        private boolean current;

        public PageItem(long number, boolean current) {
            this.number = number;
            this.current = current;
        }

        public long getNumber() {
            return this.number;
        }

        public boolean isCurrent() {
            return this.current;
        }
    }
}