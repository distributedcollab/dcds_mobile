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
package edu.mit.ll.dcds.android.adapters;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.mit.ll.dcds.android.R;
import edu.mit.ll.dcds.android.api.data.ReportSendStatus;
import edu.mit.ll.dcds.android.api.data.ResourceRequestData;
import edu.mit.ll.dcds.android.api.payload.forms.ResourceRequestPayload;

public class ResourceRequestListAdapter extends ArrayAdapter<ResourceRequestPayload> {
	private List<ResourceRequestPayload> mItems;
	private Resources mResources;
	private Context mContext;
	
	public ResourceRequestListAdapter(Context context, int resource,
			int textViewResourceId, List<ResourceRequestPayload> list) {
		
		super(context, resource, textViewResourceId, list);

		mContext = context;
		mResources = context.getResources();
		mItems = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ResourceRequestPayload payload = mItems.get(position);
		ResourceRequestData data = payload.getMessageData();
		View row = super.getView(position, convertView, parent);

		TextView name = (TextView)row.findViewById(R.id.resourceRequestTitle);
		TextView desc = (TextView)row.findViewById(R.id.resourceRequestDesc);

		if(data.getDescription() != null && !data.getDescription().isEmpty()) {
			desc.setVisibility(View.VISIBLE);
			desc.setText(data.getDescription());
		} else {
			desc.setVisibility(View.GONE);
		}
		
		TextView size = (TextView)row.findViewById(R.id.resourceRequestTime);
		size.setText(new Date(mItems.get(position).getSeqTime()).toString());
		
		if(payload.isDraft()) {
			name.setText(mContext.getString(R.string.draft) + data.getUser());
		} else if(payload.getSendStatus() == ReportSendStatus.WAITING_TO_SEND  && payload.getProgress() != 100.0) {
			if(!payload.isFailedToSend()){
				if(payload.getProgress() > 0){
					name.setText(mContext.getString(R.string.sending_progress, payload.getProgress()) + String.valueOf(data.getUser()));
				}else{
					name.setText(mContext.getString(R.string.sending) + String.valueOf(data.getUser()));
				}
			}else{
				name.setText(R.string.sending_failed + String.valueOf(data.getUser()));
			}
		} else {
			name.setText(String.valueOf(data.getUser()));
		}
		
		ImageView image = (ImageView) row.findViewById(R.id.resourceRequestThumbnail);		
		Resources resources = mContext.getResources();
		
		if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_vessel))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.vessel));
		}else if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_vehicle))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.vehicle));
		}else if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_overhead))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.overhead));
		}else if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_equipment))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.equipment));
		}else if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_aircraft))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.aircraft));
		}else if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_helo))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.helicopter));
		}else if(data.getType().equals(resources.getString(R.string.resreq_resreqtype_crew))){
			image.setImageDrawable(mResources.getDrawable(R.drawable.crew));
		}else{
			image.setImageDrawable(mResources.getDrawable(R.drawable.casualty));
		}
		
		return(row);	
	}
	
	@Override
	public void addAll(Collection<? extends ResourceRequestPayload> collection) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			super.addAll(collection);
		} else {
			for(ResourceRequestPayload payload : collection) {
				add(payload);
			}
		}
	}
}
