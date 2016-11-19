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
//
//  FilterReportListViewController.m
//  dcds Mobile
//
//

#import "FilterReportListViewController.h"

@interface FilterReportListViewController ()

@end

@implementation FilterReportListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    _dataManager = [DataManager getInstance];
    [self initSpinners];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)FilterButtonPressed:(id)sender {
    UxoReportFilterData* filter = [[UxoReportFilterData alloc]init];
    filter.UxoUnit = _UnitSpinner.getTextView.text;
    filter.UxoType = _TypeSpinner.getTextView.text;
    filter.UxoPriority = _PrioritySpinner.getTextView.text;
    filter.UxoStatus = _StatusSpinner.getTextView.text;
    filter.UxoToDate = [NSNumber numberWithLongLong:[self roundTimeStampToNearestHour:_DateToSlider.value]];
    filter.UxoFromDate = [NSNumber numberWithLongLong:[self roundTimeStampToNearestHour:_DateFromSlider.value]];
    filter.UxoDateFilterEnabled = _DateRangeFilterSwitch.isOn;
    
    [[IncidentButtonBar GetUxoReportListview] filterReports:filter];
    [_dataManager setUxoFilteringSettings:filter];
    
    if(_dataManager.isIpad){
        [self dismissViewControllerAnimated:YES completion:nil];
    }else{
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (IBAction)ResetButtonPressed:(id)sender {

    [_DateRangeFilterSwitch setOn: NO];
    
    _UnitSpinner.getTextView.text = NSLocalizedString(@"All", nil);
    _TypeSpinner.getTextView.text = NSLocalizedString(@"All", nil);
    _PrioritySpinner.getTextView.text = NSLocalizedString(@"All", nil);
    _StatusSpinner.getTextView.text = NSLocalizedString(@"All", nil);
    [_DateFromSlider setValue:[_startTimeStamp doubleValue]];
    [_DateToSlider setValue:[_endTimeStamp doubleValue]];
    [self FilterButtonPressed:nil];
}

-(void)initSpinners{
    
    NSArray *TypeOptions = [_dataManager getUxoExplosiveTypeOptions];
    NSArray *PriotityOptions = [_dataManager getUxoPriorityOptions];
    NSArray *StatusOptions = [_dataManager getUxoStatusOptions];
    NSMutableArray *UnitOptions = [_dataManager getUxoUnitFilterOptions];
    
    _startTimeStamp = [_dataManager getUxoDateRangeStart];
    _endTimeStamp = [_dataManager getUxoDateRangeEnd];

    UxoReportFilterData* savedFilter = [_dataManager getUxoFilteringSettings];
     [_DateRangeFilterSwitch setOn: savedFilter.UxoDateFilterEnabled];
    
    [_DateFromSlider setMinimumValue:[_startTimeStamp doubleValue]];
    [_DateFromSlider setMaximumValue:[_endTimeStamp doubleValue]];

    
    if(savedFilter.UxoFromDate == 0){
        [_DateFromSlider setValue:[_startTimeStamp doubleValue]];
    }else{
        [_DateFromSlider setValue:[savedFilter.UxoFromDate doubleValue]];
    }
    NSDate* initLabelFromDate = [NSDate dateWithTimeIntervalSince1970: _DateFromSlider.value/1000.0];
    _DateFromValueLabel.text = [[Utils getDateFormatter] stringFromDate:initLabelFromDate];
    
    [_DateToSlider setMinimumValue:[_startTimeStamp doubleValue]];
    [_DateToSlider setMaximumValue:[_endTimeStamp doubleValue]];
    
    if(savedFilter.UxoToDate == 0){
        [_DateToSlider setValue:[_endTimeStamp doubleValue]];
    }else{
        [_DateToSlider setValue:[savedFilter.UxoToDate doubleValue]];
    }
    NSDate* initLabelToDate = [NSDate dateWithTimeIntervalSince1970:_DateToSlider.value/1000.0];
    _DateToValueLabel.text = [[Utils getDateFormatter] stringFromDate:initLabelToDate];
    
    
    
    int spinnerWidth = 100;
    
    _UnitSpinner = [[FormSpinner alloc] initWithTitle:@"Unit" options:UnitOptions];
    [_FieldFilterView addSubview:_UnitSpinner];
    _UnitSpinner.label.text = NSLocalizedString(@"Unit",nil);
    [_UnitSpinner setSpinnerWidth: spinnerWidth];
    [_UnitSpinner setSpinnerPosition: _FieldFilterView.frame.origin.x  : _FieldFilterView.frame.origin.y];
    [_UnitSpinner getTextView].text = savedFilter.UxoUnit;
    
    _TypeSpinner = [[FormSpinner alloc] initWithTitle:@"Type" options:TypeOptions];
    [_FieldFilterView addSubview:_TypeSpinner];
    _TypeSpinner.label.text = NSLocalizedString(@"Type",nil);
    [_TypeSpinner setSpinnerWidth: spinnerWidth];
    [_TypeSpinner setSpinnerPosition: _FieldFilterView.frame.origin.x + 105 : _FieldFilterView.frame.origin.y];
    [_TypeSpinner getTextView].text = savedFilter.UxoType;
    
    
    
    _PrioritySpinner = [[FormSpinner alloc] initWithTitle:@"Priority" options:PriotityOptions];
    [_FieldFilterView addSubview:_PrioritySpinner];
    _PrioritySpinner.label.text = NSLocalizedString(@"Priority",nil);
    [_PrioritySpinner setSpinnerWidth: spinnerWidth];
    [_PrioritySpinner getTextView].text = savedFilter.UxoPriority;
    if([_dataManager isIpad]){
        [_PrioritySpinner setSpinnerPosition: _FieldFilterView.frame.origin.x + 210 : _FieldFilterView.frame.origin.y];
    }else{
        [_PrioritySpinner setSpinnerPosition: _FieldFilterView.frame.origin.x  : _FieldFilterView.frame.origin.y + 80];
    }
    
    _StatusSpinner = [[FormSpinner alloc] initWithTitle:@"Status" options:StatusOptions];
    [_FieldFilterView addSubview:_StatusSpinner];
    _StatusSpinner.label.text = NSLocalizedString(@"Status",nil);
    [_StatusSpinner setSpinnerWidth: spinnerWidth];
    
    [_StatusSpinner getTextView].text = savedFilter.UxoStatus;
    if([_dataManager isIpad]){
        [_StatusSpinner setSpinnerPosition: _FieldFilterView.frame.origin.x + 315 : _FieldFilterView.frame.origin.y];
    }else{
        [_StatusSpinner setSpinnerPosition: _FieldFilterView.frame.origin.x + 105 : _FieldFilterView.frame.origin.y + 80];
    }
}

- (IBAction)DateFromSliderAction:(id)sender {
    
    long long roundedTime = [self roundTimeStampToNearestHour:_DateFromSlider.value];
    
//    double sliderValue = ;
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:roundedTime /1000.0];
    _DateFromValueLabel.text = [[Utils getDateFormatter] stringFromDate:date];
    
    if(_DateFromSlider.value>_DateToSlider.value){
        
        _DateToSlider.value = _DateFromSlider.value;
        
        NSNumber *valueNum = [NSNumber numberWithFloat:_DateToSlider.value];
        
        NSDate* dateTo = [NSDate dateWithTimeIntervalSince1970:[valueNum longLongValue]/1000.0];
        _DateToValueLabel.text = [[Utils getDateFormatter] stringFromDate:date];
    }
}

- (IBAction)DateToSliderAction:(id)sender {
    
    long long roundedTime = [self roundTimeStampToNearestHour:_DateToSlider.value];
    
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:roundedTime/1000.0];
    _DateToValueLabel.text = [[Utils getDateFormatter] stringFromDate:date];
    
    if(_DateToSlider.value<_DateFromSlider.value){
        
        _DateFromSlider.value = _DateToSlider.value;
        
        NSDate* dateFrom = [NSDate dateWithTimeIntervalSince1970:_DateFromSlider.value/1000.0];
        _DateFromValueLabel.text = [[Utils getDateFormatter] stringFromDate:date];
    }
    
}

-(long long)roundTimeStampToNearestHour:(long long)originalTime{
    
    long long time = originalTime;
    time += 1800000;
    time = time/(3600000);
    return time*3600000;
}

-(long long)roundTimeStampToNearestDay:(long long)originalTime{
    
    long long millisecondsFromGMT = 1000 * [[NSTimeZone localTimeZone] secondsFromGMT];
    
    long long time = originalTime;
    time += 43200000;
    time = time/86400000;
    time = (time*86400000) - millisecondsFromGMT;
    return time;
    
}
@end
