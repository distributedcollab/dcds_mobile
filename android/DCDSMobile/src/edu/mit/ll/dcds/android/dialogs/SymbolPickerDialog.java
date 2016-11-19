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
/**
 *
 */
package edu.mit.ll.dcds.android.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.adapters.SymbolImageAdapter;
import edu.mit.ll.dcds.android.maps.markup.Symbols;

public class SymbolPickerDialog extends DialogFragment {

	private GridView mSymbolsView;
	private TabHost mTabHost;
	private Dialog mDialog;
	private Context mContext;
	
	public SymbolPickerDialog(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_markup_symbol, container);
		
        mSymbolsView = (GridView) view.findViewById(R.id.gridView1);
        
        mSymbolsView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("resourceId", (Integer)view.getTag());
				getTargetFragment().onActivityResult(100, 0, intent);
				dismiss();
			}
		});
        
        mTabHost = (TabHost) view.findViewById(R.id.symbolsTabHost);
        mTabHost.setup();

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				if(tabId.equals(getString(R.string.ics))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.ICS));
				} else if(tabId.equals(getString(R.string.incident))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.INCIDENT));
				}else if(tabId.equals(getString(R.string.cst))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.CST));
				}else if(tabId.equals(getString(R.string.friendly_unit))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.FRIENDLYUNIT));
				}else if(tabId.equals(getString(R.string.resource))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.RESOURCES));
				}else if(tabId.equals(getString(R.string.mission))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.MISSION));
				}else if(tabId.equals(getString(R.string.usar))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.USAR));
				}else if(tabId.equals(getString(R.string.uscg))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.USCG));
				}else if(tabId.equals(getString(R.string.esi))) {
			        mSymbolsView.setAdapter(new SymbolImageAdapter(mContext, Symbols.ESI));
				}
			}
		});
      
        TabSpec spec1 = mTabHost.newTabSpec(getString(R.string.ics));
        spec1.setContent(R.id.tab1);
        spec1.setIndicator(getString(R.string.ics));
      
        TabSpec spec2 = mTabHost.newTabSpec(getString(R.string.incident));
        spec2.setContent(R.id.personalLogTab);
        spec2.setIndicator(getString(R.string.incident));
      
        TabSpec spec3 = mTabHost.newTabSpec(getString(R.string.cst));
        spec3.setContent(R.id.tab3);
        spec3.setIndicator(getString(R.string.cst));
        
        TabSpec spec4 = mTabHost.newTabSpec(getString(R.string.friendly_unit));
        spec4.setContent(R.id.tab4);
        spec4.setIndicator(getString(R.string.friendly_unit));
        
        TabSpec spec5 = mTabHost.newTabSpec(getString(R.string.resource));
        spec5.setContent(R.id.tab5);
        spec5.setIndicator(getString(R.string.resource));
        
        TabSpec spec6 = mTabHost.newTabSpec(getString(R.string.mission));
        spec6.setContent(R.id.tab6);
        spec6.setIndicator(getString(R.string.mission));
        
        TabSpec spec7 = mTabHost.newTabSpec(getString(R.string.usar));
        spec7.setContent(R.id.tab7);
        spec7.setIndicator(getString(R.string.usar));
        
        TabSpec spec8 = mTabHost.newTabSpec(getString(R.string.uscg));
        spec8.setContent(R.id.tab8);
        spec8.setIndicator(getString(R.string.uscg));
        
        TabSpec spec9 = mTabHost.newTabSpec(getString(R.string.esi));
        spec9.setContent(R.id.tab9);
        spec9.setIndicator(getString(R.string.esi));
        
        mTabHost.addTab(spec1);
        mTabHost.addTab(spec2);
        mTabHost.addTab(spec3);
        mTabHost.addTab(spec4);
        mTabHost.addTab(spec5);
        mTabHost.addTab(spec6);
        mTabHost.addTab(spec7);
        mTabHost.addTab(spec8);
        mTabHost.addTab(spec9);
        
        mDialog = this.getDialog();
        mDialog.setTitle(R.string.symbol_picker);
        
        return view;
	}
}
