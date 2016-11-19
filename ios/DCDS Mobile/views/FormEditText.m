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
//  FormEditText.m
//  dcds_iOS
//
//

#import "FormEditText.h"

@implementation FormEditText

- (id)init
{
    self = [super init];
    [self setup];
    return self;
}

- (void)setup {
    [super setup];
    self.type = @"string";
    [[NSBundle mainBundle] loadNibNamed:@"FormEditText" owner:self options:nil];
    
    for(UIView * subview in self.view.subviews) {
        [subview setUserInteractionEnabled:YES];
        [self addSubview:subview];
    }
    self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.superview.frame.size.width, self.view.frame.size.height);
    
    [self.interactableViews addObject:self.customTextView];
    self.customTextView.returnKeyType = UIReturnKeyDefault;
//    self.field.delegate = self.view;

}

-(CustomTextView*) getTextView{
    return [self.interactableViews objectAtIndex:0];
}

-(void)setEditable:(bool)isEditable{
    _customTextView.editable = isEditable;
}

@end