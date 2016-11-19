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
package edu.mit.ll.dcds.android.formgen;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.api.data.DamageInformation;

@SuppressLint({ "UseSparseArrays", "InflateParams" })
public class FormDamageInformation extends FormWidget {
	
	private HashMap<Integer, DamageInformation> damageInformationList;
	
	private ArrayAdapter<String> mDamageAmountItems;
	private ArrayAdapter<String> mDamageTypeItems;
	
	private LayoutInflater mLayoutInflater;
	
	private boolean mIsEditable;

	private Button mAddItemButton;
	protected JSONObject mOptions;

	
	public FormDamageInformation(FragmentActivity context, String property, String displayText, JSONObject options, boolean enabled, OnFocusChangeListener listener,Fragment fragment) {
		super(context, property, displayText,fragment);
		
		mIsEditable = false;
		mLayoutInflater = LayoutInflater.from(mContext);
		
		mEnabled = enabled;
		
		mAddItemButton = new Button(mContext);
		mAddItemButton.setText(R.string.add_item);
		mAddItemButton.setBackgroundColor(Color.argb(200, 76, 153, 0));
		mAddItemButton.setTextSize(12);
		mAddItemButton.setWidth(256);
		mLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		mAddItemButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mAddItemButton.setOnClickListener(addItemListener);
		mLayout.addView(mAddItemButton, 0);
		
		mDamageAmountItems = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item) {
			public View getView(int position, View convertView, ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                
                v.setTypeface(null, Typeface.NORMAL);
                v.setTextColor(Color.WHITE);
                v.setText(mDamageAmountItems.getItem(position));
                
                return v;
            }
		};
		mDamageAmountItems.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		for(int i = 0; i < options.length(); i++) {
			try {
				Resources res = mContext.getResources();
		        int resId = res.getIdentifier(options.getString(String.valueOf(i)), "string", mContext.getPackageName());				
				mDamageAmountItems.add(res.getString(resId));
			} catch (JSONException e) {
			}
		}
		
		mDamageTypeItems = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item) {
			public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setSingleLine(false);
                
                return v;
            }
		};
		mDamageTypeItems.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		
		Resources resources = mContext.getResources();
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_notset));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_car));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_commercialmultistory));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_doublewidetrailer));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_motorhome));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_otherbarn));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_othergarage));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_otheroutbuilding));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_residencemultifamily));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_residencemultistorymultifamily));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_residencemultistorysinglefamily));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_residencesinglefamily));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_singlewidetrailer));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_trailer));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_truck));
		mDamageTypeItems.add(resources.getString(R.string.dr_propertytype_van));
				
//		for(PropertyType item : PropertyType.values()) {
//			mDamageTypeItems.add(item.getText());
//		}
	}

	@Override
	public String getValue() {
		return new Gson().toJson(damageInformationList.values());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setValue(String value) {
		
		if(value != null && !value.isEmpty() && mLayout != null) {
			ArrayList<DamageInformation> damageInformation = new Gson().fromJson(value, new TypeToken<ArrayList<DamageInformation>>(){}.getType());
			
			if(damageInformation != null) {
				mLayout.removeAllViews();
				if(mAddItemButton.getParent() == null) {
					mLayout.addView(mAddItemButton, 0);
				}
				
				damageInformationList = new HashMap<Integer, DamageInformation>();
				
				if(!mIsEditable && damageInformation.isEmpty()) {
					damageInformation.add(new DamageInformation());
				}
				
				for(int i = 0; i < damageInformation.size(); i++) {
					DamageInformation information = damageInformation.get(i);
					
					LinearLayout item = (LinearLayout) mLayoutInflater.inflate(R.layout.listitem_damage_information, null);
					item.setTag(i);
					
					Spinner damageAmountSpinner = (Spinner) item.findViewById(R.id.damageAmountSpinner);
					damageAmountSpinner.setAdapter(mDamageAmountItems);
					
					int position = mDamageAmountItems.getPosition(information.getDamageType());
					if(position != -1){
						damageAmountSpinner.setSelection(position);
					}else{
						mDamageAmountItems.add(information.getDamageType());
						damageAmountSpinner.setSelection(mDamageAmountItems.getPosition(information.getDamageType()));
					}
					
					
					Resources resources = mContext.getResources();
					
					if(information.getDamageType().equals( resources.getString(R.string.dr_propertydamagetype_blank))){
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							damageAmountSpinner.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_border));
						} else {
							damageAmountSpinner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rectangle_border));
						}
					} else if(information.getDamageType().equals( resources.getString(R.string.dr_propertydamagetype_minor))){
						damageAmountSpinner.setBackgroundColor(Color.YELLOW);
					} else if (information.getDamageType().equals( resources.getString(R.string.dr_propertydamagetype_major))){
						damageAmountSpinner.setBackgroundColor(Color.RED);
					} else {
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							damageAmountSpinner.setBackground(mContext.getResources().getDrawable(R.drawable.destroyed));
						} else {
							damageAmountSpinner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.destroyed));
						}
					}
					damageAmountSpinner.setTag(i);
					damageAmountSpinner.setOnItemSelectedListener(damageAmountSelectedListener);

					
					Spinner damageTypeSpinner = (Spinner) item.findViewById(R.id.damageTypeSpinner);
					damageTypeSpinner.setAdapter(mDamageTypeItems);
					
					int pos = mDamageTypeItems.getPosition(information.getPropertyType());
					if(pos != -1){
						damageTypeSpinner.setSelection(pos);
					}else{
						mDamageTypeItems.add(information.getDamageType());
						damageTypeSpinner.setSelection(mDamageAmountItems.getPosition(information.getDamageType()));
					}
					
					
					
					damageTypeSpinner.setTag(i);
					damageTypeSpinner.setOnItemSelectedListener(damageTypeSelectedListener);
					
					Button damageDeleteButton = (Button) item.findViewById(R.id.damageDeleteButton);
					damageDeleteButton.setTag(i);
					damageDeleteButton.setOnClickListener(deleteDamageInformationListener);
					
					damageAmountSpinner.setEnabled(mIsEditable);
					damageTypeSpinner.setEnabled(mIsEditable);
					
					if(!mIsEditable) {
						damageDeleteButton.setVisibility(View.GONE);
					} else {
						damageDeleteButton.setVisibility(View.VISIBLE);
					}
					
					mLayout.addView(item);
					information.setView(item);
					damageInformationList.put(i, information);
				}
			}
		} else {
			mLayout.removeAllViews();
			if(damageInformationList != null) {
				damageInformationList.clear();
			} else {
				damageInformationList = new HashMap<Integer, DamageInformation>();
			}
			if(mAddItemButton.getParent() == null) {
				mLayout.addView(mAddItemButton, 0);
			}
		}
	}
	
	protected OnClickListener addItemListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(damageInformationList == null) {
				damageInformationList = new HashMap<Integer, DamageInformation>();
			}
			
			Resources resources = mContext.getResources();
			createItem(resources.getString(R.string.dr_propertydamagetype_blank), resources.getString(R.string.dr_propertytype_notset));
		}
	};
	
	@SuppressWarnings("deprecation")
	protected void createItem(String damageType, String propertyType) {
		LinearLayout item = (LinearLayout) mLayoutInflater.inflate(R.layout.listitem_damage_information, null);
		int itemIndex = damageInformationList.size();
		
		item.setTag(itemIndex);
		
		Spinner damageAmountSpinner = (Spinner) item.findViewById(R.id.damageAmountSpinner);
		damageAmountSpinner.setAdapter(mDamageAmountItems);
		
		int position = mDamageAmountItems.getPosition(damageType);
		if(position != -1){
			damageAmountSpinner.setSelection(position, false);
		}else{
			mDamageAmountItems.add(damageType);
			damageAmountSpinner.setSelection(mDamageAmountItems.getPosition(damageType));
		}
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			damageAmountSpinner.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_border));
		} else {
			damageAmountSpinner.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rectangle_border));
		}
		damageAmountSpinner.setTag(itemIndex);
		damageAmountSpinner.setOnItemSelectedListener(damageAmountSelectedListener);

		Spinner damageTypeSpinner = (Spinner) item.findViewById(R.id.damageTypeSpinner);
		damageTypeSpinner.setAdapter(mDamageTypeItems);
		
		
		int pos = mDamageTypeItems.getPosition(propertyType);
		if(pos != -1){
			damageTypeSpinner.setSelection(pos);
		}else{
			mDamageTypeItems.add(propertyType);
			damageTypeSpinner.setSelection(mDamageAmountItems.getPosition(propertyType));
		}
		
		
		damageTypeSpinner.setTag(itemIndex);
		damageTypeSpinner.setOnItemSelectedListener(damageTypeSelectedListener);
		
		Button damageDeleteButton = (Button) item.findViewById(R.id.damageDeleteButton);
		damageDeleteButton.setTag(itemIndex);
		damageDeleteButton.setOnClickListener(deleteDamageInformationListener);
		
		damageAmountSpinner.setEnabled(mIsEditable);
		damageTypeSpinner.setEnabled(mIsEditable);
		
		if(!mIsEditable) {
			damageDeleteButton.setVisibility(View.GONE);
		} else {
			damageDeleteButton.setVisibility(View.VISIBLE);
		}
		
		DamageInformation newInfo = new DamageInformation();
		newInfo.setView(item);
		
		damageInformationList.put(itemIndex, newInfo);
		mLayout.addView(item);
		
		if(damageAmountSpinner.getSelectedItemPosition() == 0 && mIsEditable) {
			damageTypeSpinner.performClick();
		}
	}
	

	protected OnItemSelectedListener damageAmountSelectedListener = new OnItemSelectedListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			DamageInformation updateInfo = damageInformationList.get((Integer) parent.getTag());
			updateInfo.setDamageType(mDamageAmountItems.getItem(position));
			
			if(position == 0) {
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					parent.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_border));
				} else {
					parent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rectangle_border));
				}
			} else if(position == 1 ) {
				parent.setBackgroundColor(Color.YELLOW);
			} else if(position == 2) {
				parent.setBackgroundColor(Color.RED);
			} else if(position == 3) {
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					parent.setBackground(mContext.getResources().getDrawable(R.drawable.destroyed));
				} else {
					parent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.destroyed));
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
		
	};

	protected OnItemSelectedListener damageTypeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			DamageInformation updateInfo = damageInformationList.get((Integer) parent.getTag());
			
			
			updateInfo.setPropertyType(mDamageTypeItems.getItem(position));
			
			Spinner damageAmountSpinner = (Spinner) updateInfo.getView().findViewById(R.id.damageAmountSpinner);
			if(damageAmountSpinner.getSelectedItemPosition() == 0 && mIsEditable) {
				damageAmountSpinner.performClick();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
		
	};


	protected OnClickListener deleteDamageInformationListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DamageInformation deletedItem = damageInformationList.remove(((Integer)v.getTag()).intValue());
			mLayout.removeView(deletedItem.getView());
		}
	};

	@Override
	public void setHint(String value) {
	}

	@Override
	public void setEditable(boolean isEditable) {
		mIsEditable = isEditable;
		if(mIsEditable) {
			mAddItemButton.setVisibility(View.VISIBLE);
		} else {
			mAddItemButton.setVisibility(View.GONE);
		}
		
		if(mAddItemButton.getParent() == null) {
			mLayout.addView(mAddItemButton, 0);
		}
	}
}
