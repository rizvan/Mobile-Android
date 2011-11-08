package org.scielo.search;

import java.util.ArrayList;


public class Pagination {
	private int MAX_CELLS;

	// Parse to get translated text
	Pagination(int max_cells){
		MAX_CELLS = max_cells;
	}

	public void generatePages(ArrayList<Page> pagesList, String str_current_doc_idx, String str_qtd_item_per_page, String str_total_result){
		int current_doc_idx =str2int(str_current_doc_idx);
		int qtd_item_per_page =str2int(str_qtd_item_per_page);
		int total_result =str2int(str_total_result);
		
		boolean paginate = true;
		int missing = total_result;
		int qtd_visible = (MAX_CELLS - 2) * qtd_item_per_page;
		int batch = (current_doc_idx / qtd_visible);
		int first_of_the_batch = (batch * qtd_visible);
		int times =1;
		int k = 0;
		Page p;
		pagesList.clear();
		if (total_result>0){
			k = first_of_the_batch;
			if (first_of_the_batch>0){
				p = new Page(" < ",int2str(first_of_the_batch - qtd_visible));
			} else {
				p = new Page("   ",int2str(first_of_the_batch));
			}
			pagesList.add(p);
			
			while (paginate){
				p = new Page(int2str(k+1),int2str(k));
				pagesList.add(p);
				k = k + qtd_item_per_page;
				missing = missing - qtd_item_per_page;
				
				paginate = ((missing > 0) && (times<(MAX_CELLS-2)));
				times++;
			}
			if (missing > 0){
				p = new Page(" > " ,int2str(k));
				pagesList.add(p);
			}
		}
	}
	public String int2str(int n){
		return new Integer(n).toString();
	}
	public int str2int(String s){
		return Integer.parseInt(s);
	}
}
