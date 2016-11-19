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
package edu.mit.ll.dcds.android.fragments;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.mit.ll.dcds.android.MainActivity;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.api.DataManager;
import edu.mit.ll.dcds.android.api.data.UxoReportFilterData;
//import edu.mit.ll.dcds.android.api.data.UxoTypes;
import edu.mit.ll.dcds.android.api.payload.forms.UxoReportPayload;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class UxoFilterFragment extends Fragment {

	private DataManager mDataManager;
	
	private Spinner unitSpinner;
	private Spinner typeSpinner;
	private Spinner prioritySpinner;
	private Spinner statusSpinner;
	private TextView DateFromResultsText;
	private TextView DateToResultsText;
	private SeekBar DateFromSeekBar;
	private SeekBar DateToSeekBar;
	private Button ResetFilterButton;
	
	private long minTimeStamp = 0;
	private long maxTimeStamp = 0;
	
	 ArrayList<String> unitOptions = new ArrayList<String>();
	 ArrayList<String> typeOptions = new ArrayList<String>();
	 ArrayList<String> priorityOptions = new ArrayList<String>();
	 ArrayList<String> statusOptions = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		mDataManager = DataManager.getInstance();
		View view = inflater.inflate(R.layout.fragment_uxofilter, container, false);
		
		unitSpinner = (Spinner) view.findViewById(R.id.unitSpinner);
		typeSpinner = (Spinner) view.findViewById(R.id.typeSpinner);
		prioritySpinner = (Spinner) view.findViewById(R.id.prioritySpinner);
		statusSpinner = (Spinner) view.findViewById(R.id.statusSpinner);
		DateFromResultsText = (TextView) view.findViewById(R.id.dateFromResultsTextView);
		DateToResultsText = (TextView) view.findViewById(R.id.dateToResultsTextView);
		DateFromSeekBar = (SeekBar) view.findViewById(R.id.dateFromBar);
		DateToSeekBar = (SeekBar) view.findViewById(R.id.dateToBar);
		ResetFilterButton = (Button) view.findViewById(R.id.resetFilterButton);
		
		ArrayList<UxoReportPayload> reports = mDataManager.getUxoReportHistoryForIncident(mDataManager.getActiveIncidentId());
		
		 unitOptions = new ArrayList<String>();
		 typeOptions = new ArrayList<String>();
		 priorityOptions = new ArrayList<String>();
		 statusOptions = new ArrayList<String>();
		 
		 unitOptions.add(getString(R.string.all));
		 typeOptions.add(getString(R.string.all));
		 priorityOptions.add(getString(R.string.all));
		 statusOptions.add(getString(R.string.all));
		 
		 for(UxoReportPayload payload : reports){
			 
			 String unit = payload.getMessageData().getReportingUnit();
			 if(!unitOptions.contains(unit)){
				 unitOptions.add(unit);
			 }
			 
			 String type = payload.getMessageData().getUxoType().toString();
			 if(!typeOptions.contains(type)){
				 typeOptions.add(type);
			 }
			 
			 String priority = payload.getMessageData().getRecommendedPriority().toString();
			 if(!priorityOptions.contains(priority)){
				 priorityOptions.add(priority);
			 }
			 
			 String status = payload.getMessageData().getStatus();
			 if(status == null){
				 status = "";
			 }
			 if(!statusOptions.contains(status)){
				 statusOptions.add(status);
			 }
		 }
		 
		ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(mDataManager.getContext(), android.R.layout.simple_dropdown_item_1line, unitOptions);
		ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(mDataManager.getContext(), android.R.layout.simple_dropdown_item_1line, typeOptions);
		ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(mDataManager.getContext(), android.R.layout.simple_dropdown_item_1line, priorityOptions);
		ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(mDataManager.getContext(), android.R.layout.simple_dropdown_item_1line, statusOptions);

		unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		unitSpinner.setAdapter(unitAdapter);
		typeSpinner.setAdapter(typeAdapter);
		prioritySpinner.setAdapter(priorityAdapter);
		statusSpinner.setAdapter(statusAdapter);
		
		unitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        UxoReportFilterData.setUxoUnitCurrentFilter(unitOptions.get(position));
		    }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        UxoReportFilterData.setUxoTypeCurrentFilter(typeOptions.get(position));
		    }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		prioritySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        UxoReportFilterData.setUxoPriorityCurrentFilter(priorityOptions.get(position));
		    }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		statusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        UxoReportFilterData.setUxoStatusCurrentFilter(statusOptions.get(position));
		    }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		minTimeStamp = reports.get(0).getSeqTime();
		maxTimeStamp = reports.get(reports.size()-1).getSeqTime();

		DateFromSeekBar.setMax(10000);
		DateFromSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				setSeekBarFromDate(progress);
				if(progress >  DateToSeekBar.getProgress()){
					setSeekBarToDate(progress);
					DateToSeekBar.setProgress(progress);
				}
				if(progress <= 0 && DateToSeekBar.getProgress() >= 10000){
					UxoReportFilterData.setFilterByDateRange(false);
				}else{
					UxoReportFilterData.setFilterByDateRange(true);
				}
			}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		DateToSeekBar.setMax(10000);
		DateToSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				setSeekBarToDate(progress);
				if(progress <  DateFromSeekBar.getProgress()){
					setSeekBarFromDate(progress);
					DateFromSeekBar.setProgress(progress);
				}
				if(progress >= 10000 && DateFromSeekBar.getProgress() <= 0){
					UxoReportFilterData.setFilterByDateRange(false);
				}else{
					UxoReportFilterData.setFilterByDateRange(true);
				}
			}
			public void onStartTrackingTouch(SeekBar seekBar) {}
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		setSeekBarFromDate(0);
		setSeekBarToDate(10000);
		DateFromSeekBar.setProgress(0);
		DateToSeekBar.setProgress(10000);
		
		
		ResetFilterButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				UxoReportFilterData.setUxoFromDateCurrentFilter(0);
				UxoReportFilterData.setUxoToDateCurrentFilter(0);
				UxoReportFilterData.setUxoPriorityCurrentFilter(getString(R.string.all));
				UxoReportFilterData.setUxoTypeCurrentFilter(getString(R.string.all));
				UxoReportFilterData.setUxoUnitCurrentFilter(getString(R.string.all));
				UxoReportFilterData.setUxoStatusCurrentFilter(getString(R.string.all));
				UxoReportFilterData.setFilterByDateRange(false);
				
				unitSpinner.setSelection(0);
				typeSpinner.setSelection(0);
				prioritySpinner.setSelection(0);
				statusSpinner.setSelection(0);
				setSeekBarFromDate(0);
				setSeekBarToDate(10000);
				DateFromSeekBar.setProgress(0);
				DateToSeekBar.setProgress(10000);
			}
		});
		
		
		
		
		return view;
	}
	
	private void setSeekBarFromDate(int progress){
		long chosenTimeStamp = maxTimeStamp + ((minTimeStamp - maxTimeStamp) * progress) / ((long)10000);
		DateFromResultsText.setText(new Date(chosenTimeStamp).toString());
		UxoReportFilterData.setUxoFromDateCurrentFilter(chosenTimeStamp);
		
	}
	private void setSeekBarToDate(int progress){
		long chosenTimeStamp = maxTimeStamp + ((minTimeStamp - maxTimeStamp) * progress) / ((long)10000);
		DateToResultsText.setText(new Date(chosenTimeStamp).toString());
		UxoReportFilterData.setUxoToDateCurrentFilter(chosenTimeStamp);
	}
}
