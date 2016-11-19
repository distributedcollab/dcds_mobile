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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.fragments.FormFragment;

public class FormSlider extends FormWidget {
	protected TextView mLabel;
	protected SeekBar mInput;
	protected TextView mResult;

	public FormSlider(FragmentActivity context, String property, String displayText, boolean enabled, int max, OnFocusChangeListener listener,Fragment fragment) {
		super(context, property, displayText,fragment);

		mEnabled = enabled;
		
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		LinearLayout container = new LinearLayout(context);
		container.setLayoutParams(FormFragment.defaultLayoutParams);
		container.setOrientation(LinearLayout.HORIZONTAL);
		
		if(mDisplayTextKey != null && !mDisplayTextKey.isEmpty())  {
			mLabel = new TextView(context);
			mLabel.setText(getDisplayText());
			mLabel.setLayoutParams(FormFragment.defaultLayoutParams);
			mLayout.addView(mLabel);
		}
		
		mInput = new SeekBar (context);
		mInput.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mInput.setEnabled(enabled);
		mInput.setOnFocusChangeListener(listener);
		mInput.setTag(property);
		mInput.setMax(max);
		mInput.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_bar_green));
		
		mResult = new TextView(context);
		mResult.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mResult.setEnabled(enabled);
		mResult.setOnFocusChangeListener(listener);
		mResult.setTag(property);
		mResult.setText("0");
		mResult.setTextSize(18);
		
		if(!enabled) {
			
		}
		
		container.addView(mResult);
		container.addView(mInput);
		
		mLayout.addView(container);
		
		//default listener if one is not set in the ReportFragment
		mInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

			   @Override 
			   public void onProgressChanged(SeekBar seekBar, int progress, 
			     boolean fromUser) { 
			    // TODO Auto-generated method stub 
				   mResult.setText(String.valueOf(progress)); 
			   } 

			   @Override 
			   public void onStartTrackingTouch(SeekBar seekBar) { 
			    // TODO Auto-generated method stub 
			   } 

			   @Override 
			   public void onStopTrackingTouch(SeekBar seekBar) { 
			    // TODO Auto-generated method stub 
			   } 
	       }); 
	   } 

	public void setOnSeekBarChangedListener(SeekBar.OnSeekBarChangeListener listener){
		mInput.setOnSeekBarChangeListener(listener);
	}
	

	@Override
	public String getValue() {
		return Integer.toString(mInput.getProgress());
	}

	@Override
	public void setValue(String value) {
		if(value != null && value != ""){
			mInput.setProgress(Integer.parseInt(value));
		}else{
			mInput.setProgress(0);
		}
	}

	@Override
	public void setHint(String value) {
//		Resources res = mContext.getResources();
//        int resId = res.getIdentifier(value, "string", mContext.getPackageName());
//		mInput.setHint(res.getString(resId));
	}

	@Override
	public void setEditable(boolean isEditable) {
		mInput.setEnabled(isEditable);
		
		if(!isEditable) {
			//change visual look
		} else {
			//change visual look
		}
	}
	
	public TextView getResultTextView(){
		return mResult;
	}
	
	public SeekBar getSeekBar(){
		return mInput;
	}
}
