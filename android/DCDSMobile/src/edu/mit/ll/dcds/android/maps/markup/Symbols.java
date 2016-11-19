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

package edu.mit.ll.dcds.android.maps.markup;

import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

import edu.mit.ll.dcds.android.R;

public class Symbols {

	public static OrderedBidiMap<String, Integer> ALL;
		
	public static final OrderedBidiMap<String, Integer> ICS;
	public static final OrderedBidiMap<String, Integer> INCIDENT;
	public static final OrderedBidiMap<String, Integer> CST;
	public static final OrderedBidiMap<String, Integer> FRIENDLYUNIT;
	public static final OrderedBidiMap<String, Integer> RESOURCES;
	public static final OrderedBidiMap<String, Integer> MISSION;
	public static final OrderedBidiMap<String, Integer> USAR;
	public static final OrderedBidiMap<String, Integer> USCG;
	public static final OrderedBidiMap<String, Integer> ESI;
	
	public static final OrderedBidiMap<String, Integer> WEATHER;
	
	static {
		TreeBidiMap<String, Integer> icsTemp = new TreeBidiMap<String, Integer>();
	
		icsTemp.put("images/drawmenu/markers/fire_origin2.png",  R.drawable.fire_origin2);
		icsTemp.put("images/drawmenu/markers/icp.png",  R.drawable.icp);
		icsTemp.put("images/drawmenu/markers/incident_base2.png",  R.drawable.incident_base2);
		icsTemp.put("images/drawmenu/markers/camp.png",  R.drawable.camp);
		icsTemp.put("images/drawmenu/markers/staging_area2.png",  R.drawable.staging_area2);
		icsTemp.put("images/drawmenu/markers/water_source2.png",  R.drawable.water_source2);
		icsTemp.put("images/drawmenu/markers/division_break2.png",  R.drawable.division_break2);
		icsTemp.put("images/drawmenu/markers/branch_break2.png",  R.drawable.branch_break2);
		icsTemp.put("images/drawmenu/markers/zone-break.png",  R.drawable.zone_break);
		icsTemp.put("images/drawmenu/markers/hot_spot2.png",  R.drawable.hot_spot2);
		icsTemp.put("images/drawmenu/markers/dry_point2.png",  R.drawable.dry_point2);
		icsTemp.put("images/drawmenu/markers/heat_source2.png",  R.drawable.heat_source2);
		icsTemp.put("images/drawmenu/markers/medivac.png",  R.drawable.medivac);
		icsTemp.put("images/drawmenu/markers/helibase2.png",  R.drawable.helibase2);
		icsTemp.put("images/drawmenu/markers/helispot.png",  R.drawable.helispot);
		icsTemp.put("images/drawmenu/markers/life-hazard.png",  R.drawable.life_hazard);
		icsTemp.put("images/drawmenu/markers/safety-zone.png",  R.drawable.safety_zone);
		icsTemp.put("images/drawmenu/markers/aerial-hazard.png",  R.drawable.aerial_hazard);
		icsTemp.put("images/drawmenu/markers/repeater_mobile_relay2.png",  R.drawable.repeater_mobile_relay2);
		icsTemp.put("images/drawmenu/markers/ir-downlink.png",  R.drawable.ir_downlink);
		icsTemp.put("images/drawmenu/markers/wind.png",  R.drawable.wind);
		icsTemp.put("images/drawmenu/markers/aerial_ignition.png",  R.drawable.aerial_ignition);
		icsTemp.put("images/drawmenu/markers/F-01.png",  R.drawable.f_01);
		icsTemp.put("images/drawmenu/markers/first_aid.png",  R.drawable.first_aid);
		icsTemp.put("images/drawmenu/markers/flag-01.png",  R.drawable.flag_01);
		icsTemp.put("images/drawmenu/markers/segment-01.png",  R.drawable.segment_01);
		icsTemp.put("images/drawmenu/markers/T-01.png",  R.drawable.t_01);
		icsTemp.put("images/drawmenu/markers/X-01.png",  R.drawable.x_01);		
		
		icsTemp.put("images/drawmenu/markers/0.png",  R.drawable.text0);
		icsTemp.put("images/drawmenu/markers/1.png",  R.drawable.text1);
		icsTemp.put("images/drawmenu/markers/2.png",  R.drawable.text2);
		icsTemp.put("images/drawmenu/markers/3.png",  R.drawable.text3);
		icsTemp.put("images/drawmenu/markers/4.png",  R.drawable.text4);
		icsTemp.put("images/drawmenu/markers/5.png",  R.drawable.text5);
		icsTemp.put("images/drawmenu/markers/6.png",  R.drawable.text6);
		icsTemp.put("images/drawmenu/markers/7.png",  R.drawable.text7);
		icsTemp.put("images/drawmenu/markers/8.png",  R.drawable.text8);
		icsTemp.put("images/drawmenu/markers/9.png",  R.drawable.text9);
		
		icsTemp.put("images/drawmenu/markers/a.png",  R.drawable.a);
		icsTemp.put("images/drawmenu/markers/b.png",  R.drawable.b);
		icsTemp.put("images/drawmenu/markers/c.png",  R.drawable.c);
		icsTemp.put("images/drawmenu/markers/d.png",  R.drawable.d);
		icsTemp.put("images/drawmenu/markers/e.png",  R.drawable.e);
		icsTemp.put("images/drawmenu/markers/f.png",  R.drawable.f);
		icsTemp.put("images/drawmenu/markers/g.png",  R.drawable.g);
		icsTemp.put("images/drawmenu/markers/h.png",  R.drawable.h);
		icsTemp.put("images/drawmenu/markers/i.png",  R.drawable.i);
		icsTemp.put("images/drawmenu/markers/j.png",  R.drawable.j);
		icsTemp.put("images/drawmenu/markers/k.png",  R.drawable.k);
		icsTemp.put("images/drawmenu/markers/l.png",  R.drawable.l);
		icsTemp.put("images/drawmenu/markers/m.png",  R.drawable.m);
		icsTemp.put("images/drawmenu/markers/n.png",  R.drawable.n);
		icsTemp.put("images/drawmenu/markers/o.png",  R.drawable.o);
		icsTemp.put("images/drawmenu/markers/p.png",  R.drawable.p);
		icsTemp.put("images/drawmenu/markers/q.png",  R.drawable.q);
		icsTemp.put("images/drawmenu/markers/r.png",  R.drawable.r);
		icsTemp.put("images/drawmenu/markers/s.png",  R.drawable.s);
		icsTemp.put("images/drawmenu/markers/t.png",  R.drawable.t);
		icsTemp.put("images/drawmenu/markers/u.png",  R.drawable.u);
		icsTemp.put("images/drawmenu/markers/v.png",  R.drawable.v);
		icsTemp.put("images/drawmenu/markers/w.png",  R.drawable.w);
		icsTemp.put("images/drawmenu/markers/x.png",  R.drawable.x);
		icsTemp.put("images/drawmenu/markers/y.png",  R.drawable.y);
		icsTemp.put("images/drawmenu/markers/z.png",  R.drawable.z);
		
		icsTemp.put("images/drawmenu/markers/small_x.png",  R.drawable.small_x);
		icsTemp.put("images/drawmenu/markers/defensible_standalone_map.png",  R.drawable.defensible_standalone_map);
		icsTemp.put("images/drawmenu/markers/defensible_prep_and_hold_map.png",  R.drawable.defensible_prep_and_hold_map);
		icsTemp.put("images/drawmenu/markers/non-defensible_prep_and_leave_map.png",  R.drawable.non_defensible_prep_and_leave_map);
		icsTemp.put("images/drawmenu/markers/non-defensible_rescue_drive-by_map.png",  R.drawable.non_defensible_rescue_drive_by_map);
		
		ICS = icsTemp;
		
		TreeBidiMap<String, Integer> incidentTemp = new TreeBidiMap<String, Integer>();
		
		incidentTemp.put("images/drawmenu/markers/Civil_Disturbance_Theme_ch.png",  R.drawable.civil_disturbance_theme_ch);
		incidentTemp.put("images/drawmenu/markers/Criminal_Activity_Theme_ch.png",  R.drawable.criminal_activity_theme_ch);
		incidentTemp.put("images/drawmenu/markers/Crime_Bomb_Threat_ch.png",  R.drawable.crime_bomb_threat_ch);
		incidentTemp.put("images/drawmenu/markers/Crime_Shooting.png",  R.drawable.crime_shooting);
		incidentTemp.put("images/drawmenu/markers/Fire_Theme.png",  R.drawable.fire_theme);
		incidentTemp.put("images/drawmenu/markers/Hazmat_Hazardous_Theme.png",  R.drawable.hazmat_hazardous_theme);
		incidentTemp.put("images/drawmenu/markers/Transport_Air_Theme.png",  R.drawable.transport_air_theme);
		incidentTemp.put("images/drawmenu/markers/Transport_Marine_Theme.png",  R.drawable.transport_marine_theme);
		incidentTemp.put("images/drawmenu/markers/Transport_Rail_Theme.png",  R.drawable.transport_rail_theme);
		incidentTemp.put("images/drawmenu/markers/Transport_Vehicle_Theme.png",  R.drawable.transport_vehicle_theme);
		incidentTemp.put("images/drawmenu/markers/Geo_After_Shock.png",  R.drawable.geo_after_shock);
		incidentTemp.put("images/drawmenu/markers/Geo_Earth_Quake_Epicenter.png",  R.drawable.geo_earth_quake_epicenter);
		incidentTemp.put("images/drawmenu/markers/Geo_Landslide.png",  R.drawable.geo_landslide);
		incidentTemp.put("images/drawmenu/markers/Geo_Subsidence.png",  R.drawable.geo_subsidence);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Flood.png",  R.drawable.hydro_meteor_flood);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Fog.png",  R.drawable.hydro_meteor_fog);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Hail.png",  R.drawable.hydro_meteor_hail);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Rain.png",  R.drawable.hydro_meteor_rain);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Snow.png",  R.drawable.hydro_meteor_snow);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Thunder_Storm.png",  R.drawable.hydro_meteor_thunder_storm);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Tornado_ch.png",  R.drawable.hydro_meteor_tornado_ch);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Tropical_Cyclone.png",  R.drawable.hydro_meteor_tropical_cyclone);
		incidentTemp.put("images/drawmenu/markers/Hydro_Meteor_Tsunami_ch.png",  R.drawable.hydro_meteor_tsunami_ch);
		incidentTemp.put("images/drawmenu/markers/mil_soldier.png",  R.drawable.mil_soldier);
		incidentTemp.put("images/drawmenu/markers/mil_vehicle.png",  R.drawable.mil_vehicle);
		incidentTemp.put("images/drawmenu/markers/E_Med_Emergency_Medical_Theme_S1.png",  R.drawable.e_med_emergency_medical_theme_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_EMT_Station_Locations_S1.png",  R.drawable.e_med_emt_station_locations_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Ambulance_S1_ch.png",  R.drawable.e_med_ambulance_s1_ch);
		incidentTemp.put("images/drawmenu/markers/E_Med_Evacuation_Helicopter_Station_S1.png",  R.drawable.e_med_evacuation_helicopter_station_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Health_Department_Facility_S1.png",  R.drawable.e_med_health_department_facility_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Hospital_S1.png",  R.drawable.e_med_hospital_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Hospital_Ship_S1.png",  R.drawable.e_med_hospital_ship_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Medical_Facilities_Out_Patient_S1.png",  R.drawable.e_med_medical_facilities_out_patient_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Morgue_S1.png",  R.drawable.e_med_morgue_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Pharmacies_S1.png",  R.drawable.e_med_pharmacies_s1);
		incidentTemp.put("images/drawmenu/markers/E_Med_Triage_S1.png",  R.drawable.e_med_triage_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Operations_Theme_S1.png",  R.drawable.emergency_operations_theme_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Collection_Evacuation_Point_S1_ch.png",  R.drawable.emergency_collection_evacuation_point_s1_ch);
		incidentTemp.put("images/drawmenu/markers/Emergency_Incident_Command_Center_S1.png",  R.drawable.emergency_incident_command_center_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Operations_Center_S1.png",  R.drawable.emergency_operations_center_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Public_Information_Center_S1_ch.png",  R.drawable.emergency_public_information_center_s1_ch);
		incidentTemp.put("images/drawmenu/markers/Emergency_Shelters_S1.png",  R.drawable.emergency_shelters_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Staging_Areas_S1.png",  R.drawable.emergency_staging_areas_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Teams_S1.png",  R.drawable.emergency_teams_s1);
		incidentTemp.put("images/drawmenu/markers/Emergency_Water_Distribution_Center_S1_ch.png",  R.drawable.emergency_water_distribution_center_s1_ch);
		incidentTemp.put("images/drawmenu/markers/Emgergency_Food_Distribution_Centers_S1.png",  R.drawable.emgergency_food_distribution_centers_s1);
		incidentTemp.put("images/drawmenu/markers/Fire_Suppression_Theme_S1.png",  R.drawable.fire_suppression_theme_s1);
		incidentTemp.put("images/drawmenu/markers/Fire_Hydrant_S1.png",  R.drawable.fire_hydrant_s1);
		incidentTemp.put("images/drawmenu/markers/Fire_Other_Water_Supply_Location_S1.png",  R.drawable.fire_other_water_supply_location_s1);
		incidentTemp.put("images/drawmenu/markers/Fire_Station_S1.png",  R.drawable.fire_station_s1);
		incidentTemp.put("images/drawmenu/markers/Law_Enforcement_Theme_S1.png",  R.drawable.law_enforcement_theme_s1);
		incidentTemp.put("images/drawmenu/markers/Law_ATF_S1.png",  R.drawable.law_atf_s1);
		incidentTemp.put("images/drawmenu/markers/Law_Border_Patrol_S1.png",  R.drawable.law_border_patrol_s1);
		incidentTemp.put("images/drawmenu/markers/Law_Customs_Service_S1.png",  R.drawable.law_customs_service_s1);
		incidentTemp.put("images/drawmenu/markers/Law_DEA_S1.png",  R.drawable.law_dea_s1);
		incidentTemp.put("images/drawmenu/markers/Law_DOJ_S1.png",  R.drawable.law_doj_s1);
		incidentTemp.put("images/drawmenu/markers/Law_FBI_S1.png",  R.drawable.law_fbi_s1);
		incidentTemp.put("images/drawmenu/markers/Law_Police_S1.png",  R.drawable.law_police_s1);
		incidentTemp.put("images/drawmenu/markers/Law_Prison_S1.png",  R.drawable.law_prison_s1);
		incidentTemp.put("images/drawmenu/markers/Law_Secret_Service_S1.png",  R.drawable.law_secret_service_s1);
		incidentTemp.put("images/drawmenu/markers/Law_TSA_S1.png",  R.drawable.law_tsa_s1);
		incidentTemp.put("images/drawmenu/markers/Law_US_Coast_Guard_S1.png",  R.drawable.law_us_coast_guard_s1);
		incidentTemp.put("images/drawmenu/markers/Law_US_Marshall_S1.png",  R.drawable.law_us_marshall_s1);
				
		INCIDENT = incidentTemp;
		

		TreeBidiMap<String, Integer> cstTemp = new TreeBidiMap<String, Integer>();
		
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Incident.png",  R.drawable.incident);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Infrastructure_S1.png",  R.drawable.infrastructure_s1);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Infrastructure_S2.png",  R.drawable.infrastructure_s2);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Infrastructure_S3.png",  R.drawable.infrastructure_s3);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Infrastructure_S4.png",  R.drawable.infrastructure_s4);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Operation_S1.png",  R.drawable.operation_s1);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Operation_S2.png",  R.drawable.operation_s2);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Operation_S3.png",  R.drawable.operation_s3);
		cstTemp.put("images/drawmenu/markers/hsi/DamageOperational/Operation_S4.png",  R.drawable.operation_s4);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Blank_Incident_A.png",  R.drawable.blank_incident_a);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Blank_Incident_B.png",  R.drawable.blank_incident_b);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Civil_Demonstrations_ch.png",  R.drawable.civil_demonstrations_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Civil_Displaced_Population_ch.png",  R.drawable.civil_displaced_population_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Civil_Disturbance_Theme_ch.png",  R.drawable.civil_disturbance_theme_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Civil_Rioting_ch.png",  R.drawable.civil_rioting_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Crime_Bomb_ch.png",  R.drawable.crime_bomb_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Crime_Bomb_Explosion_ch.png",  R.drawable.crime_bomb_explosion_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Crime_Bomb_Threat_ch.png",  R.drawable.crime_bomb_threat_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Crime_Looting.png",  R.drawable.crime_looting);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Crime_Poisoning.png",  R.drawable.crime_poisoning);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Crime_Shooting.png",  R.drawable.crime_shooting);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Criminal_Activity_Theme_ch.png",  R.drawable.criminal_activity_theme_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Hotspot.png",  R.drawable.fire_hotspot);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Non_Residential_ch.png",  R.drawable.fire_non_residential_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Origin_ch.png",  R.drawable.fire_origin_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Residential_ch.png",  R.drawable.fire_residential_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_School_ch.png",  R.drawable.fire_school_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Smoke_ch.png",  R.drawable.fire_smoke_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Special_Needs.png",  R.drawable.fire_special_needs);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Theme.png",  R.drawable.fire_theme);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Fire_Wild_ch.png",  R.drawable.fire_wild_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Chemical_Agents.png",  R.drawable.hazmat_chemical_agents);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Corrosive_Material.png",  R.drawable.hazmat_corrosive_material);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Dangerous_When_Wet.png",  R.drawable.hazmat_dangerous_when_wet);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Explosive.png",  R.drawable.hazmat_explosive);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Flammable_Gas.png",  R.drawable.hazmat_flammable_gas);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Flammable_Liquid.png",  R.drawable.hazmat_flammable_liquid);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Flammable_Solid.png",  R.drawable.hazmat_flammable_solid);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Hazardous_Theme.png",  R.drawable.hazmat_hazardous_theme);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Non-Flammable_Gas.png",  R.drawable.hazmat_non_flammable_gas);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Organic_Peroxides.png",  R.drawable.hazmat_organic_peroxides);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Oxidizers.png",  R.drawable.hazmat_oxidizers);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Radioactive_Material_ch.png",  R.drawable.hazmat_radioactive_material_ch);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Spontaneously_Combustible.png",  R.drawable.hazmat_spontaneously_combustible);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Toxic_and_Infectious.png",  R.drawable.hazmat_toxic_and_infectious);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Toxic_Gas.png",  R.drawable.hazmat_toxic_gas);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Hazmat_Unexploded_Ordnance.png",  R.drawable.hazmat_unexploded_ordnance);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Air_Accident.png",  R.drawable.transport_air_accident);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Air_Hijacking.png",  R.drawable.transport_air_hijacking);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Air_Theme.png",  R.drawable.transport_air_theme);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Marine_Accident.png",  R.drawable.transport_marine_accident);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Marine_Hijacking.png",  R.drawable.transport_marine_hijacking);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Marine_Theme.png",  R.drawable.transport_marine_theme);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Rail_Accident.png",  R.drawable.transport_rail_accident);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Rail_Hijacking.png",  R.drawable.transport_rail_hijacking);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Rail_Theme.png",  R.drawable.transport_rail_theme);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Vehicle_Accident.png",  R.drawable.transport_vehicle_accident);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Vehicle_Hijacking.png",  R.drawable.transport_vehicle_hijacking);
		cstTemp.put("images/drawmenu/markers/hsi/Incidents/Transport_Vehicle_Theme.png",  R.drawable.transport_vehicle_theme);
		
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Agricultural_Laboratories_S1.png",  R.drawable.agri_agricultural_laboratories_s1);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Agricultural_Laboratories_S2.png",  R.drawable.agri_agricultural_laboratories_s2);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Agricultural_Laboratories_S3.png",  R.drawable.agri_agricultural_laboratories_s3);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Agricultural_Laboratories_S4.png",  R.drawable.agri_agricultural_laboratories_s4);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Animal_Feedlots_S1.png",  R.drawable.agri_animal_feedlots_s1);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Animal_Feedlots_S2.png",  R.drawable.agri_animal_feedlots_s2);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Animal_Feedlots_S3.png",  R.drawable.agri_animal_feedlots_s3);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Agri_Animal_Feedlots_S4.png",  R.drawable.agri_animal_feedlots_s4);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Bank_Financial_Exchanges_S1.png",  R.drawable.bank_financial_exchanges_s1);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Bank_Financial_Exchanges_S2.png",  R.drawable.bank_financial_exchanges_s2);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Bank_Financial_Exchanges_S3.png",  R.drawable.bank_financial_exchanges_s3);
		cstTemp.put("images/drawmenu/markers/hsi/Infrastructure/Bank_Financial_Exchanges_S4.png",  R.drawable.bank_financial_exchanges_s4);
		
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Blank_Natural_Event_A.png",  R.drawable.blank_natural_event_a);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Blank_Natural_Event_B.png",  R.drawable.blank_natural_event_b);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_After_Shock.png",  R.drawable.geo_after_shock);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_Avalanche_ch.png",  R.drawable.geo_avalanche_ch);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_Earth_Quake_Epicenter.png",  R.drawable.geo_earth_quake_epicenter);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_Landslide.png",  R.drawable.geo_landslide);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_Subsidence.png",  R.drawable.geo_subsidence);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_Volcanic_Eruption.png",  R.drawable.geo_volcanic_eruption);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Rain.png",  R.drawable.hydro_meteor_rain);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Inversion.png",  R.drawable.hydro_meteor_inversion);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Hail.png",  R.drawable.hydro_meteor_hail);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Fog.png",  R.drawable.hydro_meteor_fog);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Flood.png",  R.drawable.hydro_meteor_flood);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Drought.png",  R.drawable.hydro_meteor_drought);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Drizzle.png",  R.drawable.hydro_meteor_drizzle);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Geo_Volcanic_Threat.png",  R.drawable.geo_volcanic_threat);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Sand_Dust_Storm.png",  R.drawable.hydro_meteor_sand_dust_storm);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Snow.png",  R.drawable.hydro_meteor_snow);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Thunder_Storm.png",  R.drawable.hydro_meteor_thunder_storm);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Tornado_ch.png",  R.drawable.hydro_meteor_tornado_ch);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Tropical_Cyclone.png",  R.drawable.hydro_meteor_tropical_cyclone);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Hydro_Meteor_Tsunami_ch.png",  R.drawable.hydro_meteor_tsunami_ch);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Infestation_Bird_ch.png",  R.drawable.infestation_bird_ch);
		cstTemp.put("images/drawmenu/markers/hsi/NaturalEvents/Infestation_Insect.png",  R.drawable.infestation_insect);
		
		

		CST = cstTemp;
		
		TreeBidiMap<String, Integer> friendlyTemp = new TreeBidiMap<String, Integer>();
		
		friendlyTemp.put("images/drawmenu/markers/friendly/basic-chem-01.png",  R.drawable.basic_chem_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/basic-towed-01.png",  R.drawable.basic_towed_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/civil_affairs.png",  R.drawable.civil_affairs);
		friendlyTemp.put("images/drawmenu/markers/friendly/comm-01.png",  R.drawable.comm_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/cs-aviation.png",  R.drawable.cs_aviation);
		friendlyTemp.put("images/drawmenu/markers/friendly/CST-01.png",  R.drawable.cst_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/engineer-01.png",  R.drawable.engineer_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/EOD.png",  R.drawable.eod);
		friendlyTemp.put("images/drawmenu/markers/friendly/fixed_wing.png",  R.drawable.fixed_wing);
		friendlyTemp.put("images/drawmenu/markers/friendly/infantry-01.png",  R.drawable.infantry_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/mech_infantry.png",  R.drawable.mech_infantry);
		friendlyTemp.put("images/drawmenu/markers/friendly/medical_facility.png",  R.drawable.medical_facility);
		friendlyTemp.put("images/drawmenu/markers/friendly/medical.png",  R.drawable.medical_friendly);
		friendlyTemp.put("images/drawmenu/markers/friendly/mil_exploitation_uav.png",  R.drawable.mil_exploitation_uav);
		friendlyTemp.put("images/drawmenu/markers/friendly/militaryintel-01.png",  R.drawable.militaryintel_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/militarypolice-01.png",  R.drawable.militarypolice_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/mobility_enhancement.png",  R.drawable.mobility_enhancement);
		friendlyTemp.put("images/drawmenu/markers/friendly/motor_transport.png",  R.drawable.motor_transport);
		friendlyTemp.put("images/drawmenu/markers/friendly/motorized_infantry.png",  R.drawable.motorized_infantry);
		friendlyTemp.put("images/drawmenu/markers/friendly/motorized_scouts.png",  R.drawable.motorized_scouts);
		friendlyTemp.put("images/drawmenu/markers/friendly/psyop.png",  R.drawable.psyop);
		friendlyTemp.put("images/drawmenu/markers/friendly/recon_cav.png",  R.drawable.recon_cav);
		friendlyTemp.put("images/drawmenu/markers/rotary wing-01.png",  R.drawable.rotary_wing_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/specialforces-01.png",  R.drawable.specialforces_01);
		friendlyTemp.put("images/drawmenu/markers/friendly/striker_lav.png",  R.drawable.striker_lav);
		friendlyTemp.put("images/drawmenu/markers/friendly/supply_trains.png",  R.drawable.supply_trains);
		friendlyTemp.put("images/drawmenu/markers/friendly/supply_transport.png",  R.drawable.supply_transport);
		friendlyTemp.put("images/drawmenu/markers/friendly/supply.png",  R.drawable.supply);
		friendlyTemp.put("images/drawmenu/markers/friendly/survey_team.png",  R.drawable.survey_team);
		
		FRIENDLYUNIT = friendlyTemp;
		
		
		TreeBidiMap<String, Integer> resourceTemp = new TreeBidiMap<String, Integer>();
		
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/bn_med_section.png",  R.drawable.bn_med_section);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/bn_repair_evac.png",  R.drawable.bn_repair_evac);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_i_circle.png",  R.drawable.class_i_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_i.png",  R.drawable.class_i);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_ii_circle.png",  R.drawable.class_ii_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_ii.png",  R.drawable.class_ii);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_iii_circle.png",  R.drawable.class_iii_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_iii.png",  R.drawable.class_iii);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_iv_circle.png",  R.drawable.class_iv_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_iv.png",  R.drawable.class_iv);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_ix_circle.png",  R.drawable.class_ix_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_ix.png",  R.drawable.class_ix);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_v_circle.png",  R.drawable.class_v_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_v.png",  R.drawable.class_v);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_vi_circle.png",  R.drawable.class_vi_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_vi.png",  R.drawable.class_vi);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_vii_circle.png",  R.drawable.class_vii_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_vii.png",  R.drawable.class_vii);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_viii_circle.png",  R.drawable.class_viii_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_viii.png",  R.drawable.class_viii);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_x_circle.png",  R.drawable.class_x_circle);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/class_x.png",  R.drawable.class_x);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/fld_cbt_trains.png",  R.drawable.fld_cbt_trains);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/mortuary.png",  R.drawable.mortuary);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/point_maintenance.png",  R.drawable.point_maintenance);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/point.png",  R.drawable.point);
		resourceTemp.put("images/drawmenu/markers/sig_sup_serv/water.png",  R.drawable.water);
		
		RESOURCES = resourceTemp;
		
		
		TreeBidiMap<String, Integer> missionTemp = new TreeBidiMap<String, Integer>();
		
		missionTemp.put("images/drawmenu/markers/mission/CBRN-01.png",  R.drawable.cbrn_01);
		missionTemp.put("images/drawmenu/markers/mission/communications-01.png",  R.drawable.communications_01);
		missionTemp.put("images/drawmenu/markers/mission/engineering-01.png",  R.drawable.engineering_01);
		missionTemp.put("images/drawmenu/markers/mission/logistics-01.png",  R.drawable.logistics_01);
		missionTemp.put("images/drawmenu/markers/mission/medical.png",  R.drawable.medical);
		missionTemp.put("images/drawmenu/markers/mission/security.png",  R.drawable.security);
		missionTemp.put("images/drawmenu/markers/mission/transportation.png",  R.drawable.transportation);
				
		MISSION = missionTemp;

		TreeBidiMap<String, Integer> usarTemp = new TreeBidiMap<String, Integer>();
		
		usarTemp.put("images/drawmenu/markers/usar/animal-issue.png",  R.drawable.animal_issue);
		usarTemp.put("images/drawmenu/markers/usar/assisted.png",  R.drawable.assisted);
		usarTemp.put("images/drawmenu/markers/usar/confirmed-victim.png",  R.drawable.confirmed_victim);
		usarTemp.put("images/drawmenu/markers/usar/default.png",  R.drawable.default_symbol);
		usarTemp.put("images/drawmenu/markers/usar/evacuated.png",  R.drawable.evacuated);
		usarTemp.put("images/drawmenu/markers/usar/extra-21.png",  R.drawable.extra_21);
		usarTemp.put("images/drawmenu/markers/usar/extra-22.png",  R.drawable.extra_22);
		usarTemp.put("images/drawmenu/markers/usar/extra-23.png",  R.drawable.extra_23);
		usarTemp.put("images/drawmenu/markers/usar/extra-24.png",  R.drawable.extra_24);
		usarTemp.put("images/drawmenu/markers/usar/fire-incident.png",  R.drawable.fire_incident);
		usarTemp.put("images/drawmenu/markers/usar/flood-water-level.png",  R.drawable.flood_water_level);
		usarTemp.put("images/drawmenu/markers/usar/followup-form.png",  R.drawable.followup_form);
		usarTemp.put("images/drawmenu/markers/usar/hazardous-material-incident.png",  R.drawable.hazardous_material_incident);
		usarTemp.put("images/drawmenu/markers/usar/helicopter-landing-site.png",  R.drawable.helicopter_landing_site);
		usarTemp.put("images/drawmenu/markers/usar/human-remains-removed.png",  R.drawable.human_remains_removed);
		usarTemp.put("images/drawmenu/markers/usar/human-remains.png",  R.drawable.human_remains);
		usarTemp.put("images/drawmenu/markers/usar/rescued.png",  R.drawable.rescued);
		usarTemp.put("images/drawmenu/markers/usar/route-blocked.png",  R.drawable.route_blocked);
		usarTemp.put("images/drawmenu/markers/usar/shelter-in-place.png",  R.drawable.shelter_in_place);
		usarTemp.put("images/drawmenu/markers/usar/structure-damaged.png",  R.drawable.structure_damaged);
		usarTemp.put("images/drawmenu/markers/usar/structure-destroyed.png",  R.drawable.structure_destroyed);
		usarTemp.put("images/drawmenu/markers/usar/structure-failed.png",  R.drawable.structure_failed);
		usarTemp.put("images/drawmenu/markers/usar/structure-no-damage.png",  R.drawable.structure_no_damage);
		usarTemp.put("images/drawmenu/markers/usar/targeted-search.png",  R.drawable.targeted_search);
		usarTemp.put("images/drawmenu/markers/usar/victim-detected.png",  R.drawable.victim_detected);
		
		USAR = usarTemp;
			
		TreeBidiMap<String, Integer> uscgTemp = new TreeBidiMap<String, Integer>();
		
		uscgTemp.put("images/drawmenu/markers/uscg/anchorage-large.png",  R.drawable.anchorage_large);
		uscgTemp.put("images/drawmenu/markers/uscg/anchorage-small.png",  R.drawable.anchorage_small);
		uscgTemp.put("images/drawmenu/markers/uscg/anchoring-prohibited.png",  R.drawable.anchoring_prohibited);
		uscgTemp.put("images/drawmenu/markers/uscg/boat-ramp.png",  R.drawable.boat_ramp);
		uscgTemp.put("images/drawmenu/markers/uscg/boat-small.png",  R.drawable.boat_small);
		uscgTemp.put("images/drawmenu/markers/uscg/cutter-buoy.png",  R.drawable.cutter_buoy);
		uscgTemp.put("images/drawmenu/markers/uscg/cutter-large.png",  R.drawable.cutter_large);
		uscgTemp.put("images/drawmenu/markers/uscg/cutter-small.png",  R.drawable.cutter_small);
		uscgTemp.put("images/drawmenu/markers/uscg/danger-isolated.png",  R.drawable.danger_isolated);
		uscgTemp.put("images/drawmenu/markers/uscg/deployable.png",  R.drawable.deployable);
		uscgTemp.put("images/drawmenu/markers/uscg/east.png",  R.drawable.east);
		uscgTemp.put("images/drawmenu/markers/uscg/eoc.png",  R.drawable.eoc);
		uscgTemp.put("images/drawmenu/markers/uscg/fire-boom.png",  R.drawable.fire_boom);
		uscgTemp.put("images/drawmenu/markers/uscg/fixed.png",  R.drawable.fixed);
		uscgTemp.put("images/drawmenu/markers/uscg/hard-boom.png",  R.drawable.hard_boom);
		uscgTemp.put("images/drawmenu/markers/uscg/hazard-exposed.png",  R.drawable.hazard_exposed);
		uscgTemp.put("images/drawmenu/markers/uscg/hazard.png",  R.drawable.hazard);
		uscgTemp.put("images/drawmenu/markers/uscg/jic.png",  R.drawable.jic);
		uscgTemp.put("images/drawmenu/markers/uscg/joc.png",  R.drawable.joc);
		uscgTemp.put("images/drawmenu/markers/uscg/light.png",  R.drawable.light);
		uscgTemp.put("images/drawmenu/markers/uscg/marina.png",  R.drawable.marina);
		uscgTemp.put("images/drawmenu/markers/uscg/mobile-relay.png",  R.drawable.mobile_relay);
		uscgTemp.put("images/drawmenu/markers/uscg/north.png",  R.drawable.north);
		uscgTemp.put("images/drawmenu/markers/uscg/obstruction.png",  R.drawable.obstruction);
		uscgTemp.put("images/drawmenu/markers/uscg/platform.png",  R.drawable.platform);
		uscgTemp.put("images/drawmenu/markers/uscg/rotary.png",  R.drawable.rotary);
		uscgTemp.put("images/drawmenu/markers/uscg/sailboat.png",  R.drawable.sailboat);
		uscgTemp.put("images/drawmenu/markers/uscg/ship.png",  R.drawable.ship);
		uscgTemp.put("images/drawmenu/markers/uscg/skimmer.png",  R.drawable.skimmer);
		uscgTemp.put("images/drawmenu/markers/uscg/snare-boom.png",  R.drawable.snare_boom);
		uscgTemp.put("images/drawmenu/markers/uscg/sorbent-boom.png",  R.drawable.sorbent_boom);
		uscgTemp.put("images/drawmenu/markers/uscg/south.png",  R.drawable.south);
		uscgTemp.put("images/drawmenu/markers/uscg/tanker.png",  R.drawable.tanker);
		uscgTemp.put("images/drawmenu/markers/uscg/uav-fixed.png",  R.drawable.uav_fixed);
		uscgTemp.put("images/drawmenu/markers/uscg/uav-rotary.png",  R.drawable.uav_rotary);
		uscgTemp.put("images/drawmenu/markers/uscg/uscg-boat-small.png",  R.drawable.uscg_boat_small);
		uscgTemp.put("images/drawmenu/markers/uscg/uscg-deployable.png",  R.drawable.uscg_deployable);
		uscgTemp.put("images/drawmenu/markers/uscg/uscg-fixed.png",  R.drawable.uscg_fixed);
		uscgTemp.put("images/drawmenu/markers/uscg/uscg-rotary.png",  R.drawable.uscg_rotary);
		uscgTemp.put("images/drawmenu/markers/uscg/uscg-vehicle.png",  R.drawable.uscg_vehicle);
		uscgTemp.put("images/drawmenu/markers/uscg/law-enforcement.png",  R.drawable.law_enforcement);
		uscgTemp.put("images/drawmenu/markers/uscg/vehicle.png",  R.drawable.vehicle);
		uscgTemp.put("images/drawmenu/markers/uscg/west.png",  R.drawable.west);
		uscgTemp.put("images/drawmenu/markers/uscg/wreck.png",  R.drawable.wreck);
						
		USCG = uscgTemp;

		
		TreeBidiMap<String, Integer> esiTemp = new TreeBidiMap<String, Integer>();
		
		esiTemp.put("images/drawmenu/markers/esi/bird-alcid.png",  R.drawable.bird_alcid);
		esiTemp.put("images/drawmenu/markers/esi/bird-diving.png",  R.drawable.bird_diving);
		esiTemp.put("images/drawmenu/markers/esi/bird-gull.png",  R.drawable.bird_gull);
		esiTemp.put("images/drawmenu/markers/esi/bird-landfowl.png",  R.drawable.bird_landfowl);
		esiTemp.put("images/drawmenu/markers/esi/bird-passerine.png",  R.drawable.bird_passerine);
		esiTemp.put("images/drawmenu/markers/esi/bird-raptor.png",  R.drawable.bird_raptor);
		esiTemp.put("images/drawmenu/markers/esi/bird-shore.png",  R.drawable.bird_shore);
		esiTemp.put("images/drawmenu/markers/esi/bird-wading.png",  R.drawable.bird_wading);
		esiTemp.put("images/drawmenu/markers/esi/bird-waterfowl.png",  R.drawable.bird_waterfowl);
		esiTemp.put("images/drawmenu/markers/esi/fish.png",  R.drawable.fish);
		esiTemp.put("images/drawmenu/markers/esi/mmammal-bat.png",  R.drawable.mmammal_bat);
		esiTemp.put("images/drawmenu/markers/esi/mmammal-polarbear.png",  R.drawable.mmammal_polarbear);
		esiTemp.put("images/drawmenu/markers/esi/mmammal-small.png",  R.drawable.mmammal_small);
		esiTemp.put("images/drawmenu/markers/esi/mmammal-deer.png",  R.drawable.mmammal_deer);
		esiTemp.put("images/drawmenu/markers/esi/tmammal-dolphin.png",  R.drawable.tmammal_dolphin);
		esiTemp.put("images/drawmenu/markers/esi/tmammal-manatee.png",  R.drawable.tmammal_manatee);
		esiTemp.put("images/drawmenu/markers/esi/tmammal-otter.png",  R.drawable.tmammal_otter);
		esiTemp.put("images/drawmenu/markers/esi/tmammal-seal.png",  R.drawable.tmammal_seal);
		esiTemp.put("images/drawmenu/markers/esi/tmammal-whale.png",  R.drawable.tmammal_whale);
		esiTemp.put("images/drawmenu/markers/esi/invert-cephalapod.png",  R.drawable.invert_cephalapod);
		esiTemp.put("images/drawmenu/markers/esi/invert-bivalve.png",  R.drawable.invert_bivalve);
		esiTemp.put("images/drawmenu/markers/esi/invert-crab.png",  R.drawable.invert_crab);
		esiTemp.put("images/drawmenu/markers/esi/invert-echinoderm.png",  R.drawable.invert_echinoderm);
		esiTemp.put("images/drawmenu/markers/esi/invert-gastropod.png",  R.drawable.invert_gastropod);
		esiTemp.put("images/drawmenu/markers/esi/invert-insect.png",  R.drawable.invert_insect);
		esiTemp.put("images/drawmenu/markers/esi/invert-lobster.png",  R.drawable.invert_lobster);
		esiTemp.put("images/drawmenu/markers/esi/invert-shrimp.png",  R.drawable.invert_shrimp);
		esiTemp.put("images/drawmenu/markers/esi/invert-worm.png",  R.drawable.invert_worm);
		esiTemp.put("images/drawmenu/markers/esi/reptile-alligator.png",  R.drawable.reptile_alligator);
		esiTemp.put("images/drawmenu/markers/esi/reptile-other.png",  R.drawable.reptile_other);
		esiTemp.put("images/drawmenu/markers/esi/reptile-turtle.png",  R.drawable.reptile_turtle);
		esiTemp.put("images/drawmenu/markers/esi/flora-coral.png",  R.drawable.flora_coral);
		esiTemp.put("images/drawmenu/markers/esi/flora-submerged.png",  R.drawable.flora_submerged);
		esiTemp.put("images/drawmenu/markers/esi/flora-terrestrial.png",  R.drawable.flora_terrestrial);
		esiTemp.put("images/drawmenu/markers/esi/flora-floating.png",  R.drawable.flora_floating);
		esiTemp.put("images/drawmenu/markers/esi/wreck.png",  R.drawable.wreck_esi);
		esiTemp.put("images/drawmenu/markers/esi/access.png",  R.drawable.access);
		esiTemp.put("images/drawmenu/markers/esi/airport.png",  R.drawable.airport);
		esiTemp.put("images/drawmenu/markers/esi/anchorage.png",  R.drawable.anchorage);
		esiTemp.put("images/drawmenu/markers/esi/aquaculture.png",  R.drawable.aquaculture);
		esiTemp.put("images/drawmenu/markers/esi/archeological-site.png",  R.drawable.archeological_site);
		esiTemp.put("images/drawmenu/markers/esi/army-corps.png",  R.drawable.army_corps);
		esiTemp.put("images/drawmenu/markers/esi/reef-artificial.png",  R.drawable.reef_artificial);
		esiTemp.put("images/drawmenu/markers/esi/beach.png",  R.drawable.beach);
		esiTemp.put("images/drawmenu/markers/esi/boat-ramp.png",  R.drawable.boat_ramp_esi);
		esiTemp.put("images/drawmenu/markers/esi/campground.png",  R.drawable.campground);
		esiTemp.put("images/drawmenu/markers/esi/coast-guard.png",  R.drawable.coast_guard);
		esiTemp.put("images/drawmenu/markers/esi/fishing-commercial.png",  R.drawable.fishing_commercial);
		esiTemp.put("images/drawmenu/markers/esi/habitat-critical.png",  R.drawable.habitat_critical);
		esiTemp.put("images/drawmenu/markers/esi/diving.png",  R.drawable.diving);
		esiTemp.put("images/drawmenu/markers/esi/epa-facility.png",  R.drawable.epa_facility);
		esiTemp.put("images/drawmenu/markers/esi/epa-region.png",  R.drawable.epa_region);
		esiTemp.put("images/drawmenu/markers/esi/equipment.png",  R.drawable.equipment);
		esiTemp.put("images/drawmenu/markers/esi/habitat-essential.png",  R.drawable.habitat_essential);
		esiTemp.put("images/drawmenu/markers/esi/facility.png",  R.drawable.facility);
		esiTemp.put("images/drawmenu/markers/esi/factory.png",  R.drawable.factory);
		esiTemp.put("images/drawmenu/markers/esi/fema-region.png",  R.drawable.fema_region);
		esiTemp.put("images/drawmenu/markers/esi/ferry.png",  R.drawable.ferry);
		esiTemp.put("images/drawmenu/markers/esi/fishery.png",  R.drawable.fishery);
		esiTemp.put("images/drawmenu/markers/esi/heliport.png",  R.drawable.heliport);
		esiTemp.put("images/drawmenu/markers/esi/historical-site.png",  R.drawable.historical_site);
		esiTemp.put("images/drawmenu/markers/esi/invasive-species.png",  R.drawable.invasive_species);
		esiTemp.put("images/drawmenu/markers/esi/landfill.png",  R.drawable.landfill);
		esiTemp.put("images/drawmenu/markers/esi/lock-dam.png",  R.drawable.lock_dam);
		esiTemp.put("images/drawmenu/markers/esi/log-storage.png",  R.drawable.log_storage);
		esiTemp.put("images/drawmenu/markers/esi/management-area.png",  R.drawable.management_area);
		esiTemp.put("images/drawmenu/markers/esi/marina.png",  R.drawable.marina_esi);
		esiTemp.put("images/drawmenu/markers/esi/marine-sanctuary.png",  R.drawable.marine_sanctuary);
		esiTemp.put("images/drawmenu/markers/esi/military.png",  R.drawable.military);
		esiTemp.put("images/drawmenu/markers/esi/mining.png",  R.drawable.mining);
		esiTemp.put("images/drawmenu/markers/esi/mooring.png",  R.drawable.mooring);
		esiTemp.put("images/drawmenu/markers/esi/national-estuary.png",  R.drawable.national_estuary);
		esiTemp.put("images/drawmenu/markers/esi/national-forest.png",  R.drawable.national_forest);
		esiTemp.put("images/drawmenu/markers/esi/national-landmark.png",  R.drawable.national_landmark);
		esiTemp.put("images/drawmenu/markers/esi/national-park.png",  R.drawable.national_park);
		esiTemp.put("images/drawmenu/markers/esi/nature-conservancy.png",  R.drawable.nature_conservancy);
		esiTemp.put("images/drawmenu/markers/esi/noaa-facility.png",  R.drawable.noaa_facility);
		esiTemp.put("images/drawmenu/markers/esi/oil-facility.png",  R.drawable.oil_facility);
		esiTemp.put("images/drawmenu/markers/esi/oil-seep.png",  R.drawable.oil_seep);
		esiTemp.put("images/drawmenu/markers/esi/park.png",  R.drawable.park);
		esiTemp.put("images/drawmenu/markers/esi/platform.png",  R.drawable.platform_esi);
		esiTemp.put("images/drawmenu/markers/esi/port.png",  R.drawable.port);
		esiTemp.put("images/drawmenu/markers/esi/fishing-recreation.png",  R.drawable.fishing_recreation);
		esiTemp.put("images/drawmenu/markers/esi/renewable-energy.png",  R.drawable.renewable_energy);
		esiTemp.put("images/drawmenu/markers/esi/measurement-site.png",  R.drawable.measurement_site);
		esiTemp.put("images/drawmenu/markers/esi/staging.png",  R.drawable.staging);
		esiTemp.put("images/drawmenu/markers/esi/state-protected-area.png",  R.drawable.state_protected_area);
		esiTemp.put("images/drawmenu/markers/esi/storm-surge-area.png",  R.drawable.storm_surge_area);
		esiTemp.put("images/drawmenu/markers/esi/fishing-subsistence.png",  R.drawable.fishing_subsistence);
		esiTemp.put("images/drawmenu/markers/esi/surfing.png",  R.drawable.surfing);
		esiTemp.put("images/drawmenu/markers/esi/tribal-land.png",  R.drawable.tribal_land);
		esiTemp.put("images/drawmenu/markers/esi/tsunami-area.png",  R.drawable.tsunami_area);
		esiTemp.put("images/drawmenu/markers/esi/washover.png",  R.drawable.washover);
		esiTemp.put("images/drawmenu/markers/esi/waste-disposal.png",  R.drawable.waste_disposal);
		esiTemp.put("images/drawmenu/markers/esi/intake.png",  R.drawable.intake);
		esiTemp.put("images/drawmenu/markers/esi/refuge.png",  R.drawable.refuge);
		
		ESI = esiTemp;
		
		
        TreeBidiMap<String, Integer> weatherTemp = new TreeBidiMap<String, Integer>();
        weatherTemp.put("bkn.png",  R.drawable.bkn);
        weatherTemp.put("few.png",  R.drawable.few);
        weatherTemp.put("fg.png", R.drawable.fg);
        weatherTemp.put("hi_ntsra.png", R.drawable.hi_ntsra);
        weatherTemp.put("hi_tsra.png", R.drawable.hi_tsra);
        weatherTemp.put("nbknfg.png", R.drawable.nbknfg);
        weatherTemp.put("nfew.png", R.drawable.nfew);
        weatherTemp.put("nfg.png", R.drawable.nfg);
        weatherTemp.put("novc.png", R.drawable.novc);
        weatherTemp.put("nrasn.png", R.drawable.nrasn);
        weatherTemp.put("nsct.png", R.drawable.nsct);
        weatherTemp.put("nscttsra.png", R.drawable.nscttsra);
        weatherTemp.put("nshra.png", R.drawable.nshra);
        weatherTemp.put("nsn.png", R.drawable.nsn);
        weatherTemp.put("ntsra.png", R.drawable.ntsra);
        weatherTemp.put("ovc.png", R.drawable.ovc);
        weatherTemp.put("ra.png", R.drawable.ra);
        weatherTemp.put("rasn.png", R.drawable.rasn);
        weatherTemp.put("sct.png", R.drawable.sct);
        weatherTemp.put("sctfg.png", R.drawable.sctfg);
        weatherTemp.put("sctshra.png", R.drawable.sctshra);
        weatherTemp.put("scttsra.png", R.drawable.scttsra);
        weatherTemp.put("shra.png", R.drawable.shra);
        weatherTemp.put("skc.png", R.drawable.skc);
        weatherTemp.put("sn.png", R.drawable.sn);
        weatherTemp.put("tsra.png", R.drawable.tsra);
        
        WEATHER = weatherTemp;
		
		TreeBidiMap<String, Integer> allTemp = new TreeBidiMap<String, Integer>();
		allTemp.putAll(icsTemp);
		allTemp.putAll(incidentTemp);
		allTemp.putAll(cstTemp);
		allTemp.putAll(friendlyTemp);
		allTemp.putAll(resourceTemp);
		allTemp.putAll(missionTemp);
		allTemp.putAll(usarTemp);
		allTemp.putAll(uscgTemp);
		allTemp.putAll(esiTemp);

		ALL = allTemp;
	}
}
