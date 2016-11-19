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
//
//  FormEditText.h
//  dcds_iOS
//
//

#import <UIKit/UIKit.h>
#import "FormWidget.h"
#import "FormSpinner.h"
#import "DamageInformation.h"
#import "DamageInformationObject.h"

@interface FormDamageInformation : FormWidget <UITextViewDelegate, UIActionSheetDelegate>

@property NSString *title;
@property NSArray *options;

@property IBOutlet UIButton *addItemButton;
@property IBOutlet UIImageView *selectorIcon;

@property NSArray *DamageTypes;
@property NSArray *DamageAmounts;

@property UIActionSheet *DamageTypeMenu;
@property UIActionSheet *DamageAmountMenu;
@property bool ReselectingInfo;

@property NSMutableArray *DamageInfoObjects;
@property bool isDraft;

- (id)initWithTitle:(NSString *)title options:(NSArray *)options;
- (void)refreshLayout:(UIView *)view;
- (void)setData: (NSArray *) data readOnly: (bool)readOnly;
- (NSString *)getData;
- (IBAction)addData:(id)sender;
- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event;
- (void)CreateNewDamageInformationObject:(DamageInformationObject*)newDamageInfoObject: (NSString*)typeTitle;
- (void)setEditable:(bool)isEditable;
@end