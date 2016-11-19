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
//  IncidentCanvasUIViewController.m
//  dcds Mobile
//
//

#import "IncidentCanvasUIViewController.h"
#import "SimpleReportListViewController.h"
#import "SimpleReportDetailViewController.h"
#import "DamageReportListViewController.h"
#import "DamageReportDetailViewController.h"
#import "IncidentButtonBar.h"

@interface IncidentCanvasUIViewController ()

@end

@implementation IncidentCanvasUIViewController

UIViewController *selectedIncidentController = nil;

UIStoryboard *currentStoryboard;



- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
     currentStoryboard = [UIStoryboard storyboardWithName:@"Main_iPad_Prototype" bundle:nil];

    _currentReport = nil;
    
    _ReportsMenu = [[UIActionSheet alloc] initWithTitle:NSLocalizedString(@"Select Report Type",nil) delegate:self cancelButtonTitle:nil destructiveButtonTitle:nil otherButtonTitles:nil];
    
    [_ReportsMenu addButtonWithTitle:NSLocalizedString(@"Damage Report",nil)];
//    [_ReportsMenu addButtonWithTitle:NSLocalizedString(@"Resource Request",nil)];
//    [_ReportsMenu addButtonWithTitle:NSLocalizedString(@"Field Report",nil)];
    [_ReportsMenu addButtonWithTitle:NSLocalizedString(@"Explosives Report",nil)];
    [_ReportsMenu addButtonWithTitle:NSLocalizedString(@"Weather Report",nil)];
    [_ReportsMenu addButtonWithTitle:NSLocalizedString(@"Cancel",nil)];
    
    [IncidentButtonBar SetIncidentCanvasController:self];
    [IncidentButtonBar SetIncidentCanvas:_IncidentCanvas];
    
    [IncidentButtonBar SetAddButton:_AddButton];
    [IncidentButtonBar SetSaveDraftButton:_SaveDraftButton];
    [IncidentButtonBar SetCancelButton:_CancelButton];
    [IncidentButtonBar SetSubmitButton:_SubmitButton];
    [IncidentButtonBar SetFilterButton:_FilterButton];
    
    [[IncidentButtonBar GetAddButton] setHidden:TRUE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
    [[IncidentButtonBar GetFilterButton]setHidden:TRUE];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(SetCanvasToReportDetailView:) name:@"GotoReportDetailView" object:nil];
    
//    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(SetCanvasToGeneralMessage:) name:@"IncidentSwitched" object:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {

    enum ReportTypesMenu reportType = buttonIndex;
    
    switch (reportType) {
        case DamageReport:
            [self SetCanvasToDamageReport];
            break;
//        case ResourceRequest:
//            [self SetCanvasToResourceRequest];
//            break;
//        case FieldReport:
//            [self SetCanvasToFieldReport];
//            break;
        case UxoReport:
            [self SetCanvasToUxoReport];
            break;
        case WeatherReport:
            [self SetCanvasToWeatherReport];
            break;
        case Cancel:
            
            break;
            
        default:
            break;
    }
    
}

- (IBAction)ChatButtonPressed:(id)sender {

    [[IncidentButtonBar GetChatController] viewDidAppear:TRUE];
    
    _currentReport = @"Chat";
    [self SetCanvas:[IncidentButtonBar GetChatController]];
    
    [[IncidentButtonBar GetAddButton] setHidden:TRUE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
}

- (IBAction)SetCanvasToGeneralMessage:(id)sender {

    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetGeneralMessageListview] viewDidAppear:TRUE];
    
    _currentReport = @"GeneralMessage";
    [self SetCanvas:[IncidentButtonBar GetGeneralMessageListview]];
    
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
}

//Can't get ButtonBar to access the above function
- (void)SetCanvasToGeneralMessageFromButtonBar{
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetGeneralMessageListview] viewDidAppear:TRUE];
    _currentReport = @"GeneralMessage";
    [self SetCanvas:[IncidentButtonBar GetGeneralMessageListview]];
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
}

- (void)SetCanvasToDamageReport {

    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetDamageReportListview] viewDidAppear:TRUE];
    
    _currentReport = @"DamageReport";
    [self SetCanvas:[IncidentButtonBar GetDamageReportListview]];
    
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
}

- (void)SetCanvasToDamageReportFromButtonBar {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetDamageReportListview] viewDidAppear:TRUE];
    _currentReport = @"DamageReport";
    [self SetCanvas:[IncidentButtonBar GetDamageReportListview]];
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
}

- (void)SetCanvasToResourceRequest {

    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetResourceRequestListview] viewDidAppear:TRUE];
    
    _currentReport = @"ResourceRequest";
    [self SetCanvas:[IncidentButtonBar GetResourceRequestListview]];
    
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
}

- (void)SetCanvasToResourceRequestFromButtonBar {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetResourceRequestListview] viewDidAppear:TRUE];
    _currentReport = @"ResourceRequest";
    [self SetCanvas:[IncidentButtonBar GetResourceRequestListview]];
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
}

- (void)SetCanvasToFieldReport {

    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetFieldReportListview] viewDidAppear:TRUE];
    
    _currentReport = @"FieldReport";
    [self SetCanvas:[IncidentButtonBar GetFieldReportListview]];
    
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
}

- (void)SetCanvasToFieldReportFromButtonBar {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetFieldReportListview] viewDidAppear:TRUE];
    _currentReport = @"FieldReport";
    [self SetCanvas:[IncidentButtonBar GetFieldReportListview]];
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
}

- (void)SetCanvasToUxoReport {

    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetUxoReportListview] viewDidAppear:TRUE];
    
    _currentReport = @"UxoReport";
    [self SetCanvas:[IncidentButtonBar GetUxoReportListview]];
    
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:FALSE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
}

- (void)SetCanvasToWeatherReport {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetWeatherReportListview] viewDidAppear:TRUE];
    
    _currentReport = @"WeatherReport";
    [self SetCanvas:[IncidentButtonBar GetWeatherReportListview]];
    
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:FALSE];
    [[IncidentButtonBar GetSaveDraftButton]setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton]setHidden:TRUE];
    [[IncidentButtonBar GetSubmitButton]setHidden:TRUE];
}

- (void)SetCanvasToUxoReportFromButtonBar {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetUxoReportListview] viewDidAppear:TRUE];
    _currentReport = @"UxoReport";
    [self SetCanvas:[IncidentButtonBar GetUxoReportListview]];
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:FALSE];
}

- (void)SetCanvasToWeatherReportFromButtonBar {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    [[IncidentButtonBar GetWeatherReportListview] viewDidAppear:TRUE];
    _currentReport = @"WeatherReport";
    [self SetCanvas:[IncidentButtonBar GetWeatherReportListview]];
    [[IncidentButtonBar GetAddButton] setHidden:FALSE];
    [[IncidentButtonBar GetFilterButton] setHidden:FALSE];
}

- (void)SetCanvasToReportDetailView:(NSNotification *)notification {
    
    [IncidentButtonBar ClearReportDetailViews];
    
    NSMutableDictionary * reportInfo = [notification object];
    NSString* reportType = [reportInfo objectForKey:@"reportType"];
    int reportId = [[reportInfo objectForKey:@"reportId"] intValue];
    
    DataManager* dataManager = [DataManager getInstance];
    
    if([reportType isEqualToString: [Enums formTypeEnumToStringAbbrev:UXO]]){
        
        NSMutableArray *uxoReports = [dataManager getAllUxoReportsForIncidentId:[dataManager getActiveIncidentId]];
        
        for(int i = 0; i < uxoReports.count;i++){
            UxoReportPayload* payload = [uxoReports objectAtIndex:i];
            
            if([payload.id intValue] == reportId){
                
                [IncidentButtonBar GetUxoReportDetailView].payload = payload;
                [[IncidentButtonBar GetUxoReportListview] prepareForTabletCanvasSwap:FALSE:-2];
                _currentReport = @"UxoReport";
                return;
            }
        }
    }else if([reportType isEqualToString: [Enums formTypeEnumToStringAbbrev:SR]]){
        
        NSMutableArray *generalMessages = [dataManager getAllSimpleReportsForIncidentId:[dataManager getActiveIncidentId]];
        
        for(int i = 0; i < generalMessages.count;i++){
            SimpleReportPayload* payload = [generalMessages objectAtIndex:i];
            
            if([payload.id intValue] == reportId){
                
                [IncidentButtonBar GetGeneralMessageDetailView].payload = payload;
                [[IncidentButtonBar GetGeneralMessageListview] prepareForTabletCanvasSwap:FALSE:-2];
                _currentReport = @"GeneralMessage";
                return;
            }
        }
        
    }else if([reportType isEqualToString: [Enums formTypeEnumToStringAbbrev:DR]]){
        
        NSMutableArray *damageReports = [dataManager getAllDamageReportsForIncidentId:[dataManager getActiveIncidentId]];
        
        for(int i = 0; i < damageReports.count;i++){
            DamageReportPayload* payload = [damageReports objectAtIndex:i];
            
            if([payload.id intValue] == reportId){
                
                [IncidentButtonBar GetDamageReportDetailView].payload = payload;
                [[IncidentButtonBar GetDamageReportListview] prepareForTabletCanvasSwap:FALSE:-2];
                _currentReport = @"DamageReport";
                return;
            }
        }
    }else if([reportType isEqualToString: [Enums formTypeEnumToStringAbbrev:WR]]){
        
        NSMutableArray *weatherReports = [dataManager getAllWeatherReportsForIncidentId:[dataManager getActiveIncidentId]];
        
        for(int i = 0; i < weatherReports.count;i++){
            WeatherReportPayload* payload = [weatherReports objectAtIndex:i];
            
            if([payload.id intValue] == reportId){
                
                [IncidentButtonBar GetWeatherReportDetailView].payload = payload;
                [[IncidentButtonBar GetWeatherReportListview] prepareForTabletCanvasSwap:FALSE:-2];
                _currentReport = @"WeatherReport";
                return;
            }
        }
    }
}

- (void) SetCanvas:(UIViewController*)newController{
    
    selectedIncidentController = nil;
    
    selectedIncidentController = newController;
    [self addChildViewController:newController];
    
    CGRect newFrame =_IncidentCanvas.frame;
    
    newFrame.origin.y = 0;
    newFrame.origin.x = 0;
    
    newController.view.frame =newFrame;
    
    [self.IncidentCanvas addSubview:newController.view];
    [newController didMoveToParentViewController:self];
}

- (IBAction)ReportsButtonPressed:(id)sender {
    [_ReportsMenu showInView:self.parentViewController.view];
}

- (IBAction)AddButtonPressed:(id)sender {
    [IncidentButtonBar AddButtonPressed:_currentReport];
}

- (IBAction)SaveDraftButtonPressed:(id)sender {
    [IncidentButtonBar SaveDraftButtonPressed:_currentReport];
}

- (IBAction)CancelButtonPressed:(id)sender {
    [IncidentButtonBar CancelButtonPressed:_currentReport];
}

- (IBAction)SubmitButtonPressed:(id)sender {
    [IncidentButtonBar SubmitButtonPressed:_currentReport];
}

- (IBAction)FilterButtonPressed:(id)sender {
    [IncidentButtonBar FilterButtonPressed:_currentReport];
}

- (BOOL)continueTrackingWithTouch:(UITouch *)touch withEvent:(UIEvent *)event
{
    NSLog(@"Continue Tracking touch incident canvas view");
    return true;
}

- (BOOL)beginTrackingWithTouch:(UITouch *)touch withEvent:(UIEvent *)event
{
    NSLog(@"Begin Tracking touch incident canvas view");
    return true;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesBegan:touches withEvent:event];
    NSLog(@"Touches Began incident canvas view");
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesMoved:touches withEvent:event];
    NSLog(@"Touches Moved incident canvas view");
}
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    [super touchesEnded:touches withEvent:event];
    NSLog(@"Touches Ended incident canvas view");
}

@end
