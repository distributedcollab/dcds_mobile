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

#import "FormSlider.h"

@implementation FormSlider

@synthesize delegate;

- (id)init
{
    self = [super init];
    [self setup];
    return self;
}

- (void)setup {
    [super setup];
    self.type = @"slider";
    [[NSBundle mainBundle] loadNibNamed:@"FormSlider" owner:self options:nil];
    
    for(UIView * subview in self.view.subviews) {
        [subview setUserInteractionEnabled:YES];
        [self addSubview:subview];
    }
    self.view.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.superview.frame.size.width, self.view.frame.size.height);
    
    [self.interactableViews addObject:self.customTextView];
    
    self.customTextView.returnKeyType = UIReturnKeyDefault;
    //    self.field.delegate = self.view;
    
    UIPanGestureRecognizer *panRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(move:)];
    [panRecognizer setMinimumNumberOfTouches:1];
    [panRecognizer setMaximumNumberOfTouches:1];
    [self addGestureRecognizer:panRecognizer];
}

- (void)setLayout: (CGRect)formFrame{
    self.formFrame = formFrame;
    
    CGRect frame = self.view.frame;
    frame.size.width = formFrame.size.width;
    self.view.frame = frame;
    
    frame = self.slider.frame;
    frame.size.width = formFrame.size.width - self.slider.frame.origin.x - 20;
    self.slider.frame = frame;
    
    frame = self.customTextView.frame;
    frame.size.width = formFrame.size.width - self.slider.frame.origin.x - 20;
    self.customTextView.frame = frame;
}

-(CustomTextView*) getTextView{
    return [self.interactableViews objectAtIndex:0];
}

- (void)setData: (NSString *) sliderValue : (bool)readOnly{
    _readOnly = readOnly;
    
    [self.sliderValueTextView setText:sliderValue];
    [self.slider setValue:[sliderValue floatValue]];
    
    NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
    formatter.numberStyle = NSNumberFormatterDecimalStyle;
    self.previousSliderValue = [formatter numberFromString:sliderValue];
    
    if(_readOnly){
        
    }else{
        
    }
}

-(void)move:(id)sender {
    
    CGPoint point = [(UIPanGestureRecognizer*)sender locationInView:self.customTextView];
    
    CGFloat percentage = (point.x + self.slider.frame.origin.x) / self.slider.bounds.size.width;
    CGFloat delta = percentage * (self.slider.maximumValue - self.slider.minimumValue);
    NSNumber* value = [NSNumber numberWithInteger:self.slider.minimumValue + delta];
    [self.slider setValue:[value floatValue] animated:YES];
    
    NSString *str = [NSString stringWithFormat:@"%.f",[self.slider value]];
    
    [self.sliderValueTextView setText:str];

    if(value != self.previousSliderValue){
        if(delegate != nil){
            [delegate sliderMoved:str];
        }
    }
    self.previousSliderValue = value;
}

-(void)setEditable:(bool)isEditable{
    
}

- (NSString *)getData{
    return self.sliderValueTextView.text;
}

@end
