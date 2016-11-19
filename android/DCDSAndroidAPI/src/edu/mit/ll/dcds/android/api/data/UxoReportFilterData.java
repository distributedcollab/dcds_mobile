/*|~^~|Copyright (c) 2008-2016, Massachusetts Institute of Technology (MIT)
 |~^~|All rights reserved.
 |~^~|
 |~^~|Redistribution and use in source and binary forms, with or without
 |~^~|modification, are permitted provided that the following conditions are met:
 |~^~|
 |~^~|-1. Redistributions of source code must retain the above copyright notice, this
 |~^~|list of conditions and the following disclaimer.
 |~^~|
 |~^~|-2. Redistributions in binary form must reproduce the above copyright notice,
 |~^~|this list of conditions and the following disclaimer in the documentation
 |~^~|and/or other materials provided with the distribution.
 |~^~|
 |~^~|-3. Neither the name of the copyright holder nor the names of its contributors
 |~^~|may be used to endorse or promote products derived from this software without
 |~^~|specific prior written permission.
 |~^~|
 |~^~|THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 |~^~|AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 |~^~|IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 |~^~|DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 |~^~|FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 |~^~|DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 |~^~|SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 |~^~|CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 |~^~|OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 |~^~|OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\*/
package edu.mit.ll.dcds.android.api.data;

import com.google.gson.Gson;

import edu.mit.ll.dcds.android.api.DataManager;
import edu.mit.ll.dcds.android.api.R;

public class UxoReportFilterData {

	private static String UxoTypeCurrentFilter;
	private static String UxoUnitCurrentFilter;
	private static String UxoPriorityCurrentFilter;
	private static String UxoStatusCurrentFilter;
	private static long UxoFromDateCurrentFilter = 0;
	private static long UxoToDateCurrentFilter = 0;
	private static boolean FilterEnabled = false;
	private static boolean FilterByDateRange = false;
	
	public static String getUxoTypeCurrentFilter() {
		if(UxoTypeCurrentFilter == null){
			UxoTypeCurrentFilter = DataManager.getInstance().getContext().getString(R.string.all);
		}
		return UxoTypeCurrentFilter;
	}

	public static void setUxoTypeCurrentFilter(String uxoTypeCurrentFilter) {
		UxoTypeCurrentFilter = uxoTypeCurrentFilter;
	}

	public static String getUxoUnitCurrentFilter() {
		if(UxoUnitCurrentFilter == null){
			UxoUnitCurrentFilter = DataManager.getInstance().getContext().getString(R.string.all);
		}
		return UxoUnitCurrentFilter;
	}

	public static void setUxoUnitCurrentFilter(String uxoUnitCurrentFilter) {
		UxoUnitCurrentFilter = uxoUnitCurrentFilter;
	}

	public static String getUxoPriorityCurrentFilter() {
		if(UxoPriorityCurrentFilter == null){
			UxoPriorityCurrentFilter = DataManager.getInstance().getContext().getString(R.string.all);
		}
		return UxoPriorityCurrentFilter;
	}

	public static void setUxoPriorityCurrentFilter(String uxoPriorityCurrentFilter) {
		UxoPriorityCurrentFilter = uxoPriorityCurrentFilter;
	}

	public static String getUxoStatusCurrentFilter() {
		if(UxoStatusCurrentFilter == null){
			UxoStatusCurrentFilter = DataManager.getInstance().getContext().getString(R.string.all);
		}
		return UxoStatusCurrentFilter;
	}

	public static void setUxoStatusCurrentFilter(String uxoStatusCurrentFilter) {
		UxoStatusCurrentFilter = uxoStatusCurrentFilter;
	}

	public static long getUxoFromDateCurrentFilter() {
		return UxoFromDateCurrentFilter;
	}

	public static void setUxoFromDateCurrentFilter(long uxoFromDateCurrentFilter) {
		UxoFromDateCurrentFilter = uxoFromDateCurrentFilter;
	}

	public static long getUxoToDateCurrentFilter() {
		return UxoToDateCurrentFilter;
	}

	public static void setUxoToDateCurrentFilter(long uxoToDateCurrentFilter) {
		UxoToDateCurrentFilter = uxoToDateCurrentFilter;
	}

	public static boolean isFilterEnabled() {
		return FilterEnabled;
	}

	public static void setFilterEnabled(boolean filterEnabled) {
		FilterEnabled = filterEnabled;
	}
	
	public static boolean isFilterByDateRange() {
		return FilterByDateRange;
	}

	public static void setFilterByDateRange(boolean filterByDateRange) {
		FilterByDateRange = filterByDateRange;
	}
	
	public String toJsonString() {
		return new Gson().toJson(this);
	}
	
}
