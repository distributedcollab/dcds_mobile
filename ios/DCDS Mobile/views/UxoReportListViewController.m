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
#import "UxoReportListViewController.h"
#import "SWRevealViewController.h"
#import "IncidentButtonBar.h"

@interface UxoReportListViewController () {
    
}
@end

@implementation UxoReportListViewController

UIStoryboard *currentStoryboard;

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    _dataManager = [DataManager getInstance];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(viewDidAppear:) name:@"UxoReportsUpdateReceived" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(viewDidAppear:) name:@"IncidentSwitched" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(UxoReportsPolledNothing) name:@"UxoReportsPolledNothing" object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateProgress:) name:[[Enums formTypeEnumToStringAbbrev:UXO] stringByAppendingString:@"ReportProgressUpdateReceived"] object:nil];
    [_dataManager requestUxoReportsRepeatedEvery:[[DataManager getReportsUpdateFrequencyFromSettings] intValue] immediate:YES];
    
    if([_dataManager getIsIpad] == true){
        currentStoryboard = [UIStoryboard storyboardWithName:@"Main_iPad_Prototype" bundle:nil];
    }else{
        UIBarButtonItem *filterReportsBarButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemSearch target:self action:@selector(filterReportsBarButtonPressed)];

        UIBarButtonItem *addReportBarButton = [[UIBarButtonItem alloc]  initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(addReportBarButtonPressed)];
        addReportBarButton.style = UIBarButtonItemStyleBordered;
        
        self.navigationItem.rightBarButtonItems = @[addReportBarButton, filterReportsBarButton];
    }
    
    self.refreshControl = [[UIRefreshControl alloc] init];
    self.refreshControl.backgroundColor = [UIColor blackColor];
    self.refreshControl.tintColor = [UIColor whiteColor];
    [self.refreshControl addTarget:self
                            action:@selector(refreshUxoReports)
                  forControlEvents:UIControlEventValueChanged];
    
    NSDictionary *attrsDictionary = [NSDictionary dictionaryWithObject:[UIColor whiteColor]
                                                                forKey:NSForegroundColorAttributeName];
    NSAttributedString *attributedTitle = [[NSAttributedString alloc] initWithString:NSLocalizedString(@"Checking for new reports", nil)  attributes:attrsDictionary];
    self.refreshControl.attributedTitle = attributedTitle;
}

-(void)addReportBarButtonPressed{
    UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main_iPhone" bundle:nil];
    UIViewController *vc = [mainStoryboard instantiateViewControllerWithIdentifier:@"UxoReportDetailSceneID"];
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)filterReportsBarButtonPressed{
    UIStoryboard *mainStoryboard = [UIStoryboard storyboardWithName:@"Main_iPhone" bundle:nil];
    UIViewController *vc = [mainStoryboard instantiateViewControllerWithIdentifier:@"FilterReportListViewControllerID"];
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)refreshUxoReports{
    [_dataManager requestUxoReportsRepeatedEvery:[[DataManager getReportsUpdateFrequencyFromSettings]intValue] immediate:YES];
}
-(void)UxoReportsPolledNothing{
    [self.refreshControl endRefreshing];
}

- (void) updateProgress:(NSNotification *) notification {
    NSDictionary *userInfo = notification.userInfo;
    NSNumber *updatedReportId = [userInfo objectForKey:@"id"];
    
    for(UxoReportPayload *payload in _reports) {
        if(payload.id == updatedReportId) {
            payload.progress = [userInfo objectForKey:@"progress"];
            break;
        }
    }
    
    [[self tableView] reloadData];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidAppear:(BOOL)animated
{
//    _reports = [_dataManager getAllUxoReportsForIncidentId:[_dataManager getActiveIncidentId]];
    [self filterReports:[_dataManager getUxoFilteringSettings]];
    
    if(_reports.count <= 0)
    {
        UxoReportPayload* emptyListPayload = [[UxoReportPayload alloc]init];
        [_reports addObject:emptyListPayload];
        
        _emptyList = TRUE;
    }else{
        _emptyList = NO;
    }
    
    [[self tableView] reloadData];
    [self.refreshControl endRefreshing];
}

-(void)filterReports: (UxoReportFilterData*)filter{
    _reports = [_dataManager getAllUxoReportsForIncidentId:[_dataManager getActiveIncidentId]];
    _filteredList = false;
    _currentFilter = filter;
    
    [_dataManager resetUxoUnitFilterOptions];
    [_dataManager resetUxoExplosiveTypeOptions];
    [_dataManager resetUxoStatusOptions];
    [_dataManager resetUxoPriorityOptions];
    
    if(_reports.count>0){
        UxoReportPayload* payload = _reports[_reports.count-1];
        
        long long seqTime = [payload.seqtime longLongValue]; //seqTimeLongLongLong	long long	1459885298595
        long long oneDay = 43200000;
        NSNumber* startTimeStamp = [NSNumber numberWithLongLong:(seqTime - oneDay)]; //subtract one day
        
        payload = _reports[0];
        seqTime = [payload.seqtime longLongValue];
        NSNumber* endTimeStamp = [NSNumber numberWithLongLong:(seqTime + oneDay)]; //add one day
        [_dataManager setUxoDateRangeOptions:startTimeStamp :endTimeStamp];
    }
    
    for(int i = 0 ;i < _reports.count; i++){
        
        bool itemRemoved = false;
        
        UxoReportPayload* temp = _reports[i];
        UxoReportData* tempData = temp.messageData;
        if(tempData.reportingunit != nil){
            [_dataManager addUxoUnitFilterOptions:tempData.reportingunit];
        }
        if(tempData.uxotype != nil){
            [_dataManager addUxoExplosiveTypeOptions:tempData.uxotype];
        }
        if(tempData.status != nil){
            [_dataManager addUxoStatusOptions:tempData.status];
        }
        if(tempData.recommendedpriority != nil){
            [_dataManager addUxoPriorityOptions:tempData.recommendedpriority];
        }
        
        if(![filter.UxoType isEqualToString:NSLocalizedString(@"All", nil)] && itemRemoved == false){
            _filteredList = true;
            
            if(![tempData.uxotype isEqualToString:filter.UxoType]){
                [_reports removeObjectAtIndex:i];
                i--;
                itemRemoved = true;
            }
        }
        
        if(![filter.UxoPriority isEqualToString:NSLocalizedString(@"All", nil)]  && itemRemoved == false){
            _filteredList = true;
            
            if(![tempData.recommendedpriority isEqualToString:filter.UxoPriority]){
                [_reports removeObjectAtIndex:i];
                i--;
                itemRemoved = true;
            }
        }
        
        if(![filter.UxoUnit isEqualToString:NSLocalizedString(@"All", nil)]  && itemRemoved == false){
            _filteredList = true;
            
            if(![tempData.reportingunit isEqualToString:filter.UxoUnit]){
                [_reports removeObjectAtIndex:i];
                i--;
                itemRemoved = true;
            }
        }
        
        if(![filter.UxoStatus isEqualToString:NSLocalizedString(@"All", nil)]  && itemRemoved == false){
            _filteredList = true;
            
            if(![tempData.status isEqualToString:filter.UxoStatus]){
                [_reports removeObjectAtIndex:i];
                i--;
                itemRemoved = true;
            }
        }
        
        
        if(filter.UxoDateFilterEnabled && itemRemoved == false){
            
            if(temp.seqtime < filter.UxoFromDate){
                [_reports removeObjectAtIndex:i];
                i--;
                itemRemoved = true;
                _filteredList = true;
            }else if(temp.seqtime > filter.UxoToDate){
                [_reports removeObjectAtIndex:i];
                i--;
                itemRemoved = true;
                _filteredList = true;
            }
        }
    }
    
    if(_filteredList){
        UxoReportPayload* emptyListPayload = [[UxoReportPayload alloc]init];
        [_reports insertObject:emptyListPayload atIndex:0];
    }
    
    [[self tableView] reloadData];
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] init];
    
    return view;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _reports.count;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UxoReportPayload* payload = _reports[indexPath.row];
    
    UxoReportData* data = payload.messageData;
    
    NSDate* date = [NSDate dateWithTimeIntervalSince1970:[payload.seqtime longLongValue]/1000.0];
    
    ReportCell *cell = [tableView dequeueReusableCellWithIdentifier:@"Cell" forIndexPath:indexPath];
    cell.backgroundColor = [UIColor clearColor];
    cell.lat = data.latitude;
    cell.lon = data.longitude;
    cell.parent = self.navigationController;
    
    if(_filteredList && indexPath.row == 0){
        
        [cell.TimestampLabel setHidden:false];
        [cell.NameLabel setHidden:FALSE];
        [cell.NoReportMessage setHidden:TRUE];
        [cell.MapLocationButton setHidden:TRUE];
        [cell.MapLocationButton setEnabled:FALSE];
        
        NSString* dateRangeText;
        if(_currentFilter.UxoDateFilterEnabled){
            dateRangeText = [@"\nFrom: " stringByAppendingFormat:@"%@%@%@",
                             [[Utils getDateFormatter] stringFromDate:[NSDate dateWithTimeIntervalSince1970:[_currentFilter.UxoFromDate longLongValue]/1000.0]],
                             @"\nTo:     ",
                             [[Utils getDateFormatter] stringFromDate:[NSDate dateWithTimeIntervalSince1970:[_currentFilter.UxoToDate longLongValue]/1000.0]]];
        }else{
            dateRangeText = NSLocalizedString(@"All", nil);
        }
        cell.TimestampLabel.numberOfLines = 10;
        cell.TimestampLabel.text = [@"" stringByAppendingFormat:@"%@%@%@%@%@%@%@%@%@%@%@%@%@%@%@%@%@%@%@",
                          NSLocalizedString(@"Type", nil),@":",_currentFilter.UxoType,
                          @"\n",NSLocalizedString(@"Reporting Unit", nil),@":",_currentFilter.UxoUnit,
                          @"\n",NSLocalizedString(@"Recommended Priority", nil),@":",_currentFilter.UxoPriority,
                          @"\n",NSLocalizedString(@"Status", nil),@":",_currentFilter.UxoStatus,
                          @"\n",NSLocalizedString(@"Date Range", nil),@":", dateRangeText
                          ];
        cell.NameLabel.text = @"You are not seeing all available reports";

        return cell;
    }
    
    if(_emptyList){
        [cell.NoReportMessage setHidden:false];
        [cell.TimestampLabel setHidden:TRUE];
        [cell.NameLabel setHidden:TRUE];
        [cell.MapLocationButton setHidden:TRUE];
        [cell.MapLocationButton setEnabled:FALSE];
        return cell;
    }
    
    cell.TimestampLabel.numberOfLines = 1;
    if([payload.isDraft isEqual: @1]) {
        cell.NameLabel.text = [@"<" stringByAppendingFormat:@"%@%@%@",NSLocalizedString(@"Draft", nil),@">",data.user];
    } else if([payload.status isEqual:[NSNumber numberWithInt:WAITING_TO_SEND]]) {
        if(payload.progress == 0) {
            cell.NameLabel.text = [@"<" stringByAppendingFormat:@"%@%@%@",NSLocalizedString(@"Sending", nil),@">",data.user];
        }else if([payload.progress doubleValue] >= 100){
            cell.NameLabel.text = data.user;
        } else {
            cell.NameLabel.text = [@"<" stringByAppendingFormat:@"%@%.2f%@%@",NSLocalizedString(@"Sending", nil), [payload.progress doubleValue],@">",data.user];
        }
    } else {
        cell.NameLabel.text = data.user;
    }
    
    cell.TimestampLabel.text = [@"(" stringByAppendingFormat:@"%@%@%@",data.status,@")",[[Utils getDateFormatter] stringFromDate:date]];
    
    [cell.TimestampLabel setHidden:FALSE];
    [cell.NameLabel setHidden:FALSE];
    [cell.NoReportMessage setHidden:TRUE];
    [cell.MapLocationButton setTag:indexPath.row];
    [cell.MapLocationButton setHidden:FALSE];
    [cell.MapLocationButton setEnabled:TRUE];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if(_filteredList && indexPath.row==0){
        return 180;
    }
    return 50;
}

-(NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if(_emptyList){
        return nil;
    }
    if(_filteredList && indexPath.row==0){
        
        if(_dataManager.isIpad){
//            [IncidentButtonBar FilterButtonPressed:@"UxoReport"];
        }else{
            [self filterReportsBarButtonPressed];
        }
        
        return nil;
    }
    
    UIView *selectedView = [[UIView alloc]init];
    selectedView.backgroundColor = [UIColor colorWithRed:0.1953125 green:0.5 blue:0.609375 alpha:1.0];
    
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    cell.selectedBackgroundView =selectedView;
    
    UxoReportPayload* payload = _reports[indexPath.row];
    
    if([_dataManager getIsIpad] == true){    //phones currently jump into this but doesn't cause problems but should be fixed
        [self prepareForTabletCanvasSwap:[payload.isDraft boolValue]:indexPath.row];
    }
    
    return indexPath;
}

- (void)prepareForTabletCanvasSwap:(BOOL)isEdit :(NSInteger)index{
    
    UxoReportPayload *payload;
    if(_reports.count > 0 && index >= 0) {
        payload = _reports[index];
    } else if( index == -2){
        payload = [IncidentButtonBar GetUxoReportDetailView].payload;
    } else {
        payload = [[UxoReportPayload alloc]init];
    }
    
    if (isEdit==false) {
        if([payload.isDraft isEqual: @1]) {
            [IncidentButtonBar GetUxoReportDetailView].hideEditControls = NO;
            [[IncidentButtonBar GetSaveDraftButton] setHidden:FALSE];
            [[IncidentButtonBar GetSubmitButton] setHidden:FALSE];
        } else {
            [IncidentButtonBar GetUxoReportDetailView].hideEditControls = YES;
            [[IncidentButtonBar GetSaveDraftButton] setHidden:TRUE];
            [[IncidentButtonBar GetSubmitButton] setHidden:TRUE];
        }
        
    } else if (isEdit==true) {
        [IncidentButtonBar GetUxoReportDetailView].hideEditControls = NO;
        [[IncidentButtonBar GetSaveDraftButton] setHidden:FALSE];
        [[IncidentButtonBar GetSubmitButton] setHidden:FALSE];
        
    } else {
        [[IncidentButtonBar GetSaveDraftButton] setHidden:TRUE];
        [[IncidentButtonBar GetSubmitButton] setHidden:TRUE];
    }
    
    [IncidentButtonBar GetUxoReportDetailView].payload = payload;
    [[IncidentButtonBar GetUxoReportDetailView] configureView];
    
    
    [[IncidentButtonBar GetAddButton] setHidden:TRUE];
    [[IncidentButtonBar GetFilterButton] setHidden:TRUE];
    [[IncidentButtonBar GetCancelButton] setHidden:FALSE];
    
    IncidentCanvasUIViewController* canvasController =[IncidentButtonBar GetIncidentCanvasController];
    [canvasController SetCanvas:[IncidentButtonBar GetUxoReportDetailView]];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    NSString* identifier = [segue identifier];
    if([identifier isEqualToString:@"FilterMenuSegue"]){
        return;
    }
    
    UxoReportDetailViewController *detailViewController = [segue destinationViewController];
    
    NSInteger index = [self.tableView indexPathForSelectedRow].row;
    
    UxoReportPayload *payload;
    if(_reports.count > 0) {
        payload = _reports[index];
    } else {
        payload = [UxoReportPayload new];
    }
    
    if ([identifier isEqualToString:@"showUxoReportDetail"]  && !payload.isDraft) {
        detailViewController.hideEditControls = YES;
        [detailViewController setPayload:payload];
        
    } else if ([[segue identifier] isEqualToString:@"showUxoReportDetail"] && payload.isDraft) {
        detailViewController.hideEditControls = NO;
        [detailViewController setPayload:payload];
        
    } else if ([identifier isEqualToString:@"editUxoReportDetail"]) {
        detailViewController.hideEditControls = NO;
        [detailViewController setPayload:[UxoReportPayload new]];
    } else {
        detailViewController.hideEditControls = NO;
        detailViewController.payload = [UxoReportPayload new];
    }
}

@end
