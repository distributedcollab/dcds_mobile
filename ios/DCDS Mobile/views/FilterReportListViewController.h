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
//  FilterReportListViewController.h
//  dcds Mobile
//
//

#import <UIKit/UIKit.h>
#import "FormSpinner.h"
#import "IncidentButtonBar.h"
#import "UxoReportFilterData.h"

@interface FilterReportListViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIView *FieldFilterView;
@property (weak, nonatomic) IBOutlet UIView *DateFilterView;
@property (weak, nonatomic) IBOutlet UILabel *DateFromLabel;
@property (weak, nonatomic) IBOutlet UILabel *DateToLabel;

@property (weak, nonatomic) IBOutlet UISlider *DateFromSlider;
@property (weak, nonatomic) IBOutlet UISlider *DateToSlider;
@property (weak, nonatomic) IBOutlet UILabel *DateFromValueLabel;
@property (weak, nonatomic) IBOutlet UILabel *DateToValueLabel;

@property (weak, nonatomic) IBOutlet UISwitch *DateRangeFilterSwitch;

@property DataManager *dataManager;

@property FormSpinner *UnitSpinner;
@property FormSpinner *TypeSpinner;
@property FormSpinner *PrioritySpinner;
@property FormSpinner *StatusSpinner;

@property FormSpinner *fromMonthSpinner;
@property FormSpinner *fromDaySpinner;
@property FormSpinner *fromYearSpinner;

@property FormSpinner *toMonthSpinner;
@property FormSpinner *toDaySpinner;
@property FormSpinner *toYearSpinner;

@property NSNumber* startTimeStamp;
@property NSNumber* endTimeStamp;
//@property float startToEndDifference;

@property (weak, nonatomic) IBOutlet UIButton *FilterButton;
- (IBAction)FilterButtonPressed:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *ResetButton;
- (IBAction)ResetButtonPressed:(id)sender;

- (IBAction)DateFromSliderAction:(id)sender;
- (IBAction)DateToSliderAction:(id)sender;

-(void)initSpinners;

@end
