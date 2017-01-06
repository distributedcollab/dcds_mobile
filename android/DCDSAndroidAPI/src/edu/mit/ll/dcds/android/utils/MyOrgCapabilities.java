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
package edu.mit.ll.dcds.android.utils;

public class MyOrgCapabilities {
	
	boolean RESC = false;
	boolean ABC = false;
	boolean two15 = false;
	boolean SITREP = false;
	boolean ASSGN = false;
	boolean SR = true;
	boolean FR = false;
	boolean TASK = false;
	boolean RESREQ = false;
	boolean ROC = false;
	boolean nine110 = false;
	boolean UXO = true;
	boolean DMGRPT = true;
	boolean MITAM = false;
	boolean WR = true;
	boolean MIV = false;
	boolean CENSUS = false;
	boolean WA = false;
	boolean CHAT = true;
			
	public void resetCapabilitiesToOff(){
		 RESC = false;
		 ABC = false;
		 two15 = false;
		 SITREP = false;
		 ASSGN = false;
		 SR = false;
		 FR = false;
		 TASK = false;
		 RESREQ = false;
		 ROC = false;
		 nine110 = false;
		 UXO = false;
		 DMGRPT = false;
		 MITAM = false;
		 WR = false;
		 MIV = false;
		 CENSUS = false;
		 WA = false;
		 CHAT = false;
	}
	public void resetCapabilitiesToOn(){
//		 RESC = true;
//		 ABC = true;
//		 two15 = true;
//		 SITREP = true;
//		 ASSGN = true;
		 SR = true;
//		 FR = true;
//		 TASK = true;
//		 RESREQ = true;
//		 ROC = true;
//		 nine110 = true;
		 UXO = true;
		 DMGRPT = true;
//		 MITAM = true;
		 WR = true;
//		 MIV = true;
//		 CENSUS = true;
//		 WA = true;
		 CHAT = true;
	}
	
	public boolean isRESC() {
		return RESC;
	}
	public void setRESC(boolean rESC) {
		RESC = rESC;
	}
	public boolean isABC() {
		return ABC;
	}
	public void setABC(boolean aBC) {
		ABC = aBC;
	}
	public boolean isTwo15() {
		return two15;
	}
	public void setTwo15(boolean two15) {
		this.two15 = two15;
	}
	public boolean isSITREP() {
		return SITREP;
	}
	public void setSITREP(boolean sITREP) {
		SITREP = sITREP;
	}
	public boolean isASSGN() {
		return ASSGN;
	}
	public void setASSGN(boolean aSSGN) {
		ASSGN = aSSGN;
	}
	public boolean isSR() {
		return SR;
	}
	public void setSR(boolean sR) {
		SR = sR;
	}
	public boolean isFR() {
		return FR;
	}
	public void setFR(boolean fR) {
		FR = fR;
	}
	public boolean isTASK() {
		return TASK;
	}
	public void setTASK(boolean tASK) {
		TASK = tASK;
	}
	public boolean isRESREQ() {
		return RESREQ;
	}
	public void setRESREQ(boolean rESREQ) {
		RESREQ = rESREQ;
	}
	public boolean isROC() {
		return ROC;
	}
	public void setROC(boolean rOC) {
		ROC = rOC;
	}
	public boolean isNine110() {
		return nine110;
	}
	public void setNine110(boolean nine110) {
		this.nine110 = nine110;
	}
	public boolean isUXO() {
		return UXO;
	}
	public void setUXO(boolean uXO) {
		UXO = uXO;
	}
	public boolean isDMGRPT() {
		return DMGRPT;
	}
	public void setDMGRPT(boolean dMGRPT) {
		DMGRPT = dMGRPT;
	}
	public boolean isMITAM() {
		return MITAM;
	}
	public void setMITAM(boolean mITAM) {
		MITAM = mITAM;
	}
	public boolean isWR() {
		return WR;
	}
	public void setWR(boolean wR) {
		WR = wR;
	}
	public boolean isMIV() {
		return MIV;
	}
	public void setMIV(boolean mIV) {
		MIV = mIV;
	}
	public boolean isCENSUS() {
		return CENSUS;
	}
	public void setCENSUS(boolean cENSUS) {
		CENSUS = cENSUS;
	}
	public boolean isWA() {
		return WA;
	}
	public void setWA(boolean wA) {
		WA = wA;
	}
	public boolean isCHAT() {
		return CHAT;
	}
	public void setCHAT(boolean cHAT) {
		CHAT = cHAT;
	}	
}
