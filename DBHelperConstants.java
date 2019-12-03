/**
 * Copyright (c) 2005 - 2006 Pari Networks, Inc.  All Rights Reserved.
 *
 * This software is the proprietary information of Pari Networks, Inc.
 *
 */

package com.pari.nm.utils.db;



/**
 * The Class DBHelperConstants.
 */
public class DBHelperConstants {

	/**
	 * The Constant NODE_UPDATE.
	 */
//	DE57684	- Reference BUG ID: CSCut91169:Discovery of 5000+ devices shows inconsistent results

	static final String NODE_UPDATE = "UPDATE nodes SET " +
	" family=?, vendor=?, state=?,  os=?, device_type=?, devicesource=?, status=?, ts_discovered=?, workflow_id=?,ping_status=?,snmp_status=?,isManaged=? WHERE " +
	" id=?";
	
	/**
	 * The Constant NODE_INSERT.
	 */
	static final String NODE_INSERT = "INSERT INTO nodes " +
	"(ipaddress, os, family, vendor, state, ts_discovered, device_type, devicesource,status,workflow_id,ping_status,snmp_status,isManaged) " +
	" VALUES (?,?, ?,?,?,?,?,?,?,?,?,?,?)";
	
	
	
	/**
	 * The Constant UPDATE_INVENTORY_TIME.
	 */
	static final String UPDATE_INVENTORY_TIME = "UPDATE nodes SET ts_inventoried=? WHERE id=?";
	
	/**
	 * The Constant UPDATE_CONFIG_UPDATE_TIME.
	 */
	static final String UPDATE_CONFIG_UPDATE_TIME = "UPDATE nodes SET ts_configchanged=? WHERE id=?";
	
	/**
	 * The Constant IOSNODE_IF_INSERT.
	 */
	static final String IOSNODE_IF_INSERT = "INSERT INTO ios_if " +
	"(id, if_name, interface_type_name, macaddress, slot, port, sub_port, speed, mtu, ipaddress, netmask, admin_status, oper_status, media_type) " +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant PIXNODE_IF_INSERT.
	 */
	static final String PIXNODE_IF_INSERT = "INSERT INTO pix_if " +
	"(id, if_name, if_name_alias, security_level) " +
	" VALUES (?,?,?,?)";
	
	/**
	 * The Constant IOS_VERSION_INSERT.
	 */
	static final String IOS_VERSION_INSERT = "INSERT INTO ios_version " +
	"(id, version,  build, productid, devicetype, hostname, flash, memory, serial_number, imagefile) " +
	" VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant PIX_VERSION_INSERT.
	 */
	static final String PIX_VERSION_INSERT = "INSERT INTO pix_version " +
	"(id, version, majorVersion, minorVersion, maintVersion, maintRebuildId, technologyId, technologyIdVer,  build, productid, devicetype, hostname, flash, memory) " +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant NODE_CAPABILITIES_INSERT.
	 */
	static final String NODE_CAPABILITIES_INSERT = "INSERT INTO node_not_supp_caps " +
	"(device_id, non_supp_cap_list) " + " VALUES(?,?)";
	
	/**
	 * The Constant IOS_PROMPTS_INSERT.
	 */
	static final String IOS_PROMPTS_INSERT =
	"INSERT INTO ios_node_cmd_prompts " +
	"(ipaddress, userid_prompt, password_prompt, command_prompt, enable_prompt) " +
	" VALUES (?,?,?,?,?)";
	
	/**
	 * The Constant IOS_RUNCONF_INSERT.
	 */
	static final String IOS_RUNCONF_INSERT = "INSERT INTO ios_run_conf " +
	"(id, conf_part, run_conf, conf_type) " + " VALUES (?,?,?,?)";
	
	/**
	 * The Constant NODE_DISOVERED.
	 */
	static final String NODE_DISOVERED = "select ID from nodes where ts_discovered=?";
	
	/**
	 * The Constant GROUPS_INSERT.
	 */
	static final String GROUPS_INSERT = "INSERT INTO groups " +
	"(grp_name, grp_descr, grp_type, element_type, parent_id ) " +
	" VALUES (?,?,?,?,?)";
	
	/**
	 * The Constant NODE_ID.
	 */
	public static final String NODE_ID = "select ID from nodes where ";
	
	/**
	 * The Constant IOS_VERSION_GET.
	 */
	public static final String IOS_VERSION_GET = "select * from ios_version where ";

	/**
	 * The Constant GROUPS_UPDATE.
	 */
	static final String GROUPS_UPDATE = "UPDATE groups SET " +
	" grp_name=?, grp_descr=?, grp_type=?, grp_sub_type=?, element_type=?, parent_id=?, customer_id=? WHERE grp_id=?";

	/**
	 * The Constant GRP_DYNAMIC_RULES_INSERT.
	 */
	static final String GRP_DYNAMIC_RULES_INSERT = "INSERT INTO groups " +
	"(grp_id, rule_class, class_variable, operator, compare_with ) " +
	" VALUES (?, ?,?,?,?)";
	
	/**
	 * The Constant GRP_DYNAMIC_RULES_UPDATE.
	 */
	static final String GRP_DYNAMIC_RULES_UPDATE =
	"UPDATE grp_dynamic_rules SET " +
	" rule_class=?, class_variable=?, operator=?, compare_with=? WHERE grp_id=?";
	
	/**
	 * The Constant INSERT_USER.
	 */
	static final String INSERT_USER = "INSERT INTO user_details " +
	"(login, password, name, email, phone, pager, create_customers, unlimited_access, group_id, auth_type ) " +
	" VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant UPDATE_USER.
	 */
//	fix for CSCva31359
	static final String UPDATE_USER = "UPDATE user_details SET " +
	" login=?, password=?, name=?, email=?, phone=?, pager=?, create_customers=?, unlimited_access=?, group_id=?,last_pwd_change_time=? WHERE user_id=?";
	
	/**
	 * The Constant UPDATE_PASSWORD TRACKER.
	 */
	static final String UPDATE_PASSWORD_TRACKER = "INSERT into password_tracker  " +
	"(userid,storedpassword,salt,updatedTime)"+" VALUES (?,?,?,?)";
	
	/**
	 * The Constant OLD Password.
	 */
	static final String LOAD_OLD_PASSWORD= "select * from password_tracker where  userid = ?";
	
	/**
	 * The Constant INSERT_SYSTEM_CREDENTIAL_SET.
	 */
	static final String INSERT_SYSTEM_CREDENTIAL_SET = "INSERT INTO system_credentials " +
	"(cred_set_name, ipaddress_expr, protocol, port, cipher, userid, password, enable_username, enable_password, rd_community, wr_community, v3_username, v3_engine_id, v3_auth_protocol, v3_auth_passphrase, v3_priv_protocol, v3_priv_passphrase, use_for_apply, cred_order, creator_uid, exclude_ip_expr,workflow_id,source,db_server,db_ipaddr,db_name) " +
	" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * The Constant INSERT_JUMP_SERVER_CREDENTIAL_SET.
	 */
	static final String INSERT_JUMP_SERVER_CREDENTIAL_SET = "INSERT INTO jump_server_credentials " +
	"(jump_server_ip,description, username, password, alldevices, properties) " +" VALUES (?,?,?,?,?,?)";	
	
	/**
	 * The Constant INSERT_DEVICE_TO_JUMP_SERVER_MAPPING.
	 */
	static final String INSERT_DEVICE_TO_JUMP_SERVER_MAPPING = "INSERT INTO device_to_jumpserver_mapping " +
	"(device_ip, jump_server_ip) " + " VALUES (?,?)";
	
	static final String DELETE_DEVICE_TO_JUMP_SERVER_MAPPING = "DELETE FROM device_to_jumpserver_mapping WHERE jump_server_ip = ? ";
	
	/**
	 * The Constant UPDATE_SYSTEM_CREDENTIAL_SET.
	 */
	static final String UPDATE_SYSTEM_CREDENTIAL_SET = "UPDATE system_credentials SET " +
	" cred_set_name=?, ipaddress_expr=?, protocol=?, port=?, cipher=?, userid=?, password=?, enable_username=?, enable_password=?, rd_community=?, wr_community=?, v3_username=?, v3_engine_id=?, v3_auth_protocol=?, v3_auth_passphrase=?, v3_priv_protocol=?, v3_priv_passphrase=?, creator_uid=?, use_for_apply=?, cred_order=?, exclude_ip_expr=?,workflow_id=?,source=?,db_server=?,db_ipaddr=?,db_name=? WHERE cred_entry_id=?";

	/**
	 * The Constant UPDATE_CREDENTIAL_SET.
	 */
	static final String UPDATE_JUMP_SERVER_CREDENTIAL_SET = "UPDATE jump_server_credentials SET " +
	"alldevices=?,description=?, jump_server_ip=?, username=?, password=?, properties=? WHERE cred_entry_id=?";
	
	/**
	 * The Constant INSERT_JOB_RUNS.
	 */
	static final String INSERT_JOB_RUNS =
	"INSERT INTO pari_job_runs (jobid, job_runid, job_run_state,previous_run_state, job_start_time, job_end_time, parent_job_runid) " +
	" VALUES (?,?,?,?,?,?,?)";
	
	/**
	 * The Constant UPDATE_JOB_RUNS.
	 */
	static final String UPDATE_JOB_RUNS = "UPDATE pari_job_runs SET " +
	" job_run_state=?, job_end_time=?  WHERE jobid=? AND job_runid=?";
	
	/**
	 * The Constant INSERT_JOB_RUN_LOGS.
	 */
	public static final String INSERT_JOB_RUN_LOGS =
	"INSERT INTO pari_job_logs (jobid, job_runid, seq_num, job_log) " +
	" VALUES (?,?,?,?)";
	
	/**
	 * The Constant READ_PROFILES.
	 */
	static final String READ_PROFILES = "select * from flt_profiles";
	
	/**
	 * The Constant INSERT_PROFILES.
	 */
	static final String INSERT_PROFILES =
	"insert into flt_profiles (profile_id, profile_name) VALUES (?)";
	
	/**
	 * The Constant DELETE_PROFILES.
	 */
	static final String DELETE_PROFILES =
	"delete from flt_profiles where profile_id=?";
	
	/**
	 * The Constant UPDATE_PROFILES.
	 */
	static final String UPDATE_PROFILES =
	"update flt_profiles set profile_name=? where profile_id=?";
	
	/**
	 * The Constant READ_DEVICE_PROFILES.
	 */
	static final String READ_DEVICE_PROFILES =
	"select * from flt_device_profiles";
	
	/**
	 * The Constant INSERT_DEVICE_PROFILES.
	 */
	static final String INSERT_DEVICE_PROFILES =
	"insert into flt_device_profiles (device_id, profile_id) VALUES (?,?)";
	
	/**
	 * The Constant UPDATE_DEVICE_PROFILES.
	 */
	static final String UPDATE_DEVICE_PROFILES =
	"update flt_device_profiles SET profile_id=? WHERE device_id=?";
	
	/**
	 * The Constant DELETE_DEVICE_PROFILES.
	 */
	static final String DELETE_DEVICE_PROFILES =
	"delete from flt_device_profiles where device_id=?";
	
	/**
	 * The Constant READ_PROFILE_FLT_POLICY.
	 */
	static final String READ_PROFILE_FLT_POLICY =
	"select * from flt_profile_policy";
	
	/**
	 * The Constant INSERT_PROFILE_FLT_POLICY.
	 */
	static final String INSERT_PROFILE_FLT_POLICY =
	"insert into flt_profile_policy (profile_id, flt_condition, flt_priority) VALUES (?,?,?)";
	
	/**
	 * The Constant UPDATE_PROFILE_FLT_POLICY.
	 */
	static final String UPDATE_PROFILE_FLT_POLICY =
	"update flt_profile_policy SET flt_condition=? AND flt_priority=? WHERE profile_id=?";
	
	/**
	 * The Constant DELETE_PROFILE_FLT_POLICY.
	 */
	static final String DELETE_PROFILE_FLT_POLICY =
	"delete from flt_profile_policy where profile_id=?  and flt_condition=?";
	
	/**
	 * The Constant READ_FLT_PARAMS.
	 */
	static final String READ_FLT_PARAMS = "select * from flt_params";
	
	/**
	 * The Constant INSERT_FLT_PARAMS.
	 */
	static final String INSERT_FLT_PARAMS =
	"insert into flt_params (profile_id, flt_condition, flt_param_name, flt_param_value) VALUES (?,?,?,?)";
	
	/**
	 * The Constant UPDATE_FLT_PARAMS.
	 */
	static final String UPDATE_FLT_PARAMS =
	"update flt_params SET flt_param_name=? AND flt_param_value=? WHERE profile_id=? AND flt_condition=?";
	
	/**
	 * The Constant DELETE_FLT_PARAMS.
	 */
	static final String DELETE_FLT_PARAMS =
	"delete from flt_params where profile_id=?  AND flt_condition=?";
	
	/**
	 * The Constant READ_DEVICE_FAULTS.
	 */
	static final String READ_DEVICE_FAULTS = "select * from device_faults order by flt_id";
	
	/**
	 * The Constant INSERT_DEVICE_FAULTS.
	 */
	static final String INSERT_DEVICE_FAULTS =
	"insert into device_faults (flt_id, dev_id, sub_module, flt_condition, flt_msg, flt_severity, flt_state, flt_inv_count, flt_first_reported_ts, flt_last_updated_ts) VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant UPDATE_DEVICE_FAULTS.
	 */
	static final String UPDATE_DEVICE_FAULTS =
	"update device_faults SET flt_msg=?, flt_severity=?, flt_state=?, flt_inv_count=?, flt_first_reported_ts=?, flt_last_updated_ts=? WHERE flt_id=?";

	 /**
    * The Constant INSERT_SEED_FILE_STATUS.
    */
    static final String INSERT_SEED_FILE_STATUS = "insert into seed_file_status (ipaddress, seed_file_entries,import_format, unmanage_status)  VALUES (?, ?, ?,?)";
    static final String INSERT_SEED_FILE_JOB_DETAILS = "insert into seed_file_job_details (job_id,seed_file_name,seed_file,seed_file_descr, seed_file_format,device_grp,file_size,created_user,job_start_time,job_end_time,encrypted)  VALUES (?,?, ?,?, ?, ?, ?, ?, ?, ?, ?)";
	/**
	 * The Constant DELETE_DEVICE_FAULTS.
	 */
	static final String DELETE_DEVICE_FAULTS =
	"delete from device_faults where flt_id=?";
	
	/**
	 * The Constant READ_CURRENT_DEVICE_FAULTS.
	 */ 
	static final String READ_CURRENT_DEVICE_FAULTS =
	"select * from device_current_faults";
	
	/**
	 * The Constant INSERT_CURRENT_DEVICE_FAULTS.
	 */
	static final String INSERT_CURRENT_DEVICE_FAULTS =
	"insert into device_current_faults (dev_id, flt_id, flt_condition) VALUES (?,?, ?)";
	
	/**
	 * The Constant DELETE_CURRENT_DEVICE_FAULTS.
	 */
	static final String DELETE_CURRENT_DEVICE_FAULTS =
	"delete from device_current_faults where flt_id=?";
	
	/**
	 * The Constant LOAD_POLICY_GROUPS.
	 */
	static final String LOAD_POLICY_GROUPS = "select * from policy_groups";
	
	/**
	 * The Constant INSERT_POLICY_GROUPS.
	 */
	static final String INSERT_POLICY_GROUPS =
	"insert into policy_groups (group_name, group_descr, group_title, creator_id) VALUES (?,?,?,?)";
	
	/**
	 * The Constant INSERT_POLICY_GROUPS_WITHID.
	 */
	static final String INSERT_POLICY_GROUPS_WITHID =
	"insert into policy_groups (group_id, group_name, group_title, group_descr, creator_id) VALUES (?,?,?,?,?)";
	
	/**
	 * The Constant UPDATE_POLICY_GROUPS.
	 */
	static final String UPDATE_POLICY_GROUPS =
	"update policy_groups SET group_name=?, group_descr=? where group_id=?";
	
	/**
	 * The Constant DELETE_POLICY_GROUPS.
	 */
	static final String DELETE_POLICY_GROUPS =
	"delete from policy_groups where group_id=?";
	
	/**
	 * The Constant LOAD_POLICY_GROUP_MEMBERS.
	 */
	static final String LOAD_POLICY_GROUP_MEMBERS =
	"select * from policy_group_members";
	
	/**
	 * The Constant INSERT_POLICY_GROUP_MEMBERS.
	 */
	static final String INSERT_POLICY_GROUP_MEMBERS =
	"insert into policy_group_members (group_id, policy_id) VALUES (?,?)";
	
	/**
	 * The Constant DELETE_POLICY_GROUP_MEMBERS.
	 */
	static final String DELETE_POLICY_GROUP_MEMBERS =
	"delete from policy_group_members where group_id=? and policy_id=?";
	
	/**
	 * The Constant DELETE_ALL_POLICY_GROUP_MEMBERS.
	 */
	static final String DELETE_ALL_POLICY_GROUP_MEMBERS =
	"delete from policy_group_members where group_id=?";
	
	/**
	 * The Constant INSERT_GROUPS.
	 */
	static final String INSERT_GROUPS =
	"insert into groups (grp_name, grp_descr, grp_system, grp_type, grp_sub_type, element_type, parent_id, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * The Constant INSERT_GROUP_MEMBERSHIP.
	 */
	static final String INSERT_GROUP_MEMBERSHIP =
	"insert into grp_membership (grp_id, member) VALUES (?, ?)";
	
	/**
	 * The Constant LOAD_POLICIES.
	 */
	static final String LOAD_POLICIES = "select * from policies";
	
	/**
	 * The Constant INSERT_POLICIES.
	 */
	static final String INSERT_POLICIES =
	"insert into policies (policy_name, policy_descr) VALUES (?, ?)";
	
	/**
	 * The Constant UPDATE_POLICIES.
	 */
	static final String UPDATE_POLICIES =
	"update policies SET policy_Name=?, policy_descr=? where policy_id=?";
	
	/**
	 * The Constant DELETE_POLICIES.
	 */
	static final String DELETE_POLICIES =
	"delete from policies where policy_id=?";
	
	/**
	 * The Constant LOAD_POLICY_INPUTS.
	 */
	static final String LOAD_POLICY_INPUTS = "select * from policy_inputs";
	
	/**
	 * The Constant INSERT_POLICY_INPUTS.
	 */
	static final String INSERT_POLICY_INPUTS =
	"insert into policy_inputs (group_id, policy_id, rule_identifier, key_values, var_name, var_value) VALUES (?,?, ?, ?, ?,?)";
	
	/**
	 * The Constant UPDATE_POLICY_INPUTS.
	 */
	static final String UPDATE_POLICY_INPUTS =
	"upsate policy_inputs SET var_value=? where group_id=? AND policy_id=?  AND var_name=?";
	
	/**
	 * The Constant DELETE_POLICY_INPUTS.
	 */
	static final String DELETE_POLICY_INPUTS =
	"delete from policy_inputs where group_id = ? AND policy_id=?  AND rule_identifier=? AND var_name=?";
	
	/**
	 * The Constant DELETE_POLICY_INPUTS_ALL.
	 */
	static final String DELETE_POLICY_INPUTS_ALL =
	"delete from policy_inputs where group_id = ? AND policy_id=?";
	
	/**
	 * The Constant DELETE_ALL_POLICY_INPUTS.
	 */
	static final String DELETE_ALL_POLICY_INPUTS =
	"delete from policy_inputs where group_id = ?";
	
	/**
	 * The Constant INSERT_AUDIT_RUNS.
	 */
	static final String INSERT_AUDIT_RUNS = "insert into audit_prof_runs (prof_id, job_id, job_run_id, user_id, run_time, risk_factor) VALUES (?,?,?,?,?,?)";
	
	/**
	 * The Constant INSERT_AUDIT_RESULTS.
	 */
	static final String INSERT_AUDIT_RESULTS = "insert into audit_prof_results (row_id, device_id, num_p1, num_p2, num_p3, num_p4, num_p5, selected_rules, passed_rules, failed_rules, na_rules, results) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant INSERT_MASTER_POLICY_VIOLATIONS.
	 */
	static final String INSERT_MASTER_POLICY_VIOLATIONS = "insert into master_audit_violations (row_id, policy_grp_id, policy_id, device_id, device_version, service_reference, rule_name, message, severity, fixable, violation_type, service_attr_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant INSERT_POLICY_PROFILES.
	 */
	static final String INSERT_POLICY_PROFILES = "insert into policy_profiles (prof_name, prof_description, creator_id, customer_id) VALUES (?,?,?,?)";
	
	/**
	 * The Constant UPDATE_POLICY_PROFILES.
	 */
	static final String UPDATE_POLICY_PROFILES = "update  policy_profiles set prof_name=?, prof_description=?, creator_id=?, customer_id=? where prof_id=?";
	
	/**
	 * The Constant DELETE_POLICY_PROFILES.
	 */
	static final String DELETE_POLICY_PROFILES = "delete from policy_profiles where prof_id=?";
	
	/**
	 * The Constant INSERT_POLICY_PROFILES_MEMBERS.
	 */
	static final String INSERT_POLICY_PROFILES_MEMBERS = "insert into policy_profiles_members (prof_id, policy_grp_id, member_id) VALUES (?,?,?)";
	
	/**
	 * The Constant DELETE_POLICY_PROFILES_MEMBERS.
	 */
	static final String DELETE_POLICY_PROFILES_MEMBERS = "delete from policy_profiles_members where prof_id = ? AND policy_grp_id=?";
	
	/**
	 * The Constant DELETE_POLICY_PROFILES_MEMBER.
	 */
	static final String DELETE_POLICY_PROFILES_MEMBER = "delete from policy_profiles_members where prof_id = ? AND policy_grp_id=? AND member_id=?";
   
   /**
    * The Constant INSERT_VIOLATIONS_SERVICE_REFS.
    */
   static final String INSERT_VIOLATIONS_SERVICE_REFS = "insert into violation_service_refs (violation_id, service_reference, cvssVer, cvss) VALUES (?,?,?,?)";

	// Added addl entry
	/**
	 * The Constant INSERT_IP_ROUTE.
	 */
	static final String INSERT_IP_ROUTE = "INSERT INTO ip_route_table (device_id, routeType, route, prefix, nextHop, ifName) VALUES (?,?,?,?,?,?)";
	
	/**
	 * The Constant DELETE_IP_ROUTE.
	 */
	static final String DELETE_IP_ROUTE = "DELETE FROM ip_route_table where device_id = ?";
	
	/**
	 * The Constant INSERT_INTF_DATA.
	 */
	static final String INSERT_INTF_DATA = "INSERT INTO device_intf_stats (device_id, intfName, inPackets, inBytes, outPackets, outBytes, inPacketsPerSec, inBytesPerSec, outPacketsPerSec, outBytesPerSec, inputErrors, crc, outputErrors, overRun, underRuns, runts, giants, lostcarrier,nocarrier ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant INSERT_ROUTER_VLAN_DATA.
	 */
	static final String INSERT_ROUTER_VLAN_DATA = "INSERT INTO device_show_l3_vlans (vlan_id, vlan_trunk_interface, vlan_native, protocol, address, received, transmitted, device_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * The Constant INSERT_SWITCH_VLAN_DATA.
	 */
	static final String INSERT_SWITCH_VLAN_DATA    = "INSERT INTO device_show_l2_vlans (vlan_id, vlan_name, vlan_status, vlan_ports, device_id) values (?, ?, ?, ?, ?)";
	
	/**
	 * The Constant INSERT_DEVICE_NEIGHBOR.
	 */
	static final String INSERT_DEVICE_NEIGHBOR = "insert into device_cdp_table (device_id, toipaddress, fromport, toport) VALUES (?,?,?,?)";
	
	/**
	 * The Constant DELETE_DEVICE_NEIGHBOT.
	 */
	static final String DELETE_DEVICE_NEIGHBOT = "delete from device_cdp_table where linkid=?";
	
	/**
	 * The Constant IOS_XML_AUDIT_RESULTS.
	 */
	static final String IOS_XML_AUDIT_RESULTS = "INSERT INTO xml_audit_results (job_id, taskname, results_part, xml_audit )  VALUES (?,?,?,?)";
	
	/**
	 * The Constant GET_USER_PREFERENCES.
	 */
	static final String GET_USER_PREFERENCES = "select * from  user_prefs where user_id = ?  order by sequence";
	
	/**
	 * The Constant GET_USER_PREFERENCE.
	 */
	static final String GET_USER_PREFERENCE = "select * from  user_prefs where user_id = ?  AND prefname=? order by sequence";
	
	/**
	 * The Constant INSERT_USER_PREFERENCES.
	 */
	static final String INSERT_USER_PREFERENCES = "insert into user_prefs (user_id, sequence, prefname, prefvalue)  VALUES (?,?,?,?)";
	//Added acl entries
	/**
	 * The Constant INSERT_ACL_DATA.
	 */
	static final String INSERT_ACL_DATA = "INSERT INTO device_acl_table (device_id, aclId, aceData, aclData, matches) VALUES (?,?,?,?,?)";
	//static final String INSERT_PIX_ACL_DATA = "INSERT INTO device_acl_table (device_id, aclId, aceData, aclData, matches) VALUES (?,?,?,?,?)";
	/**
	 * The Constant INSERT_PIX_SSH_STATE_DATA.
	 */
	static final String INSERT_PIX_SSH_STATE_DATA = "INSERT INTO device_pix_ssh_state_table (device_id, ssh_state, timeout, version) VALUES (?,?,?,?)";
	
	/**
	 * The Constant INSERT_PIX_SSH_DATA.
	 */
	static final String INSERT_PIX_SSH_DATA = "INSERT INTO device_pix_ssh_table (device_id, ipaddr, netmask, inout) VALUES (?,?,?,?)";
	
	/**
	 * The Constant DEVICE_OPERATION_TIME_INSERT.
	 */
	static final String DEVICE_OPERATION_TIME_INSERT = "INSERT INTO device_operation_time " +
	   "(dev_id, module, sub_module, last_updated_ts) " +
	   " VALUES (?,?,?,?)";
	
	/**
	 * The Constant INSERT_SYSLOG_MESSAGE.
	 */
	static final String INSERT_SYSLOG_MESSAGE = " insert into syslog_messages VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant INSERT_IP_TO_ID.
	 */
	static final String INSERT_IP_TO_ID = " insert into ip_to_id VALUES (?,?)";
	
	/**
	 * The Constant READ_FLT_NOTIFICATION_PREFS.
	 */
	public static final String READ_FLT_NOTIFICATION_PREFS = "select * from fault_notification_prefs";
	
	/**
	 * The Constant UPDATE_FLT_NOTIFICATION_PREFS.
	 */
	public static final String UPDATE_FLT_NOTIFICATION_PREFS = "insert into fault_notification_prefs (flt_profile_id, flt_condition, notification_enabled)  VALUES (?,?,?) ";
	
	/**
	 * The Constant UPDATE_SMTP_SETTINGS.
	 */
	public static final String UPDATE_SMTP_SETTINGS = "insert into smtp_settings (smtp_host, smtp_port, smtp_login, smtp_password, mail_from, use_ssl, emails)  VALUES (?,?,?,?,?,?,?) ";
	
	/**
	 * The Constant SAVE_ARCHIVE_PREFEERENCES.
	 */
	public static final String SAVE_ARCHIVE_PREFEERENCES = "insert into archive_preferences (module_name, interval)  VALUES (?,?)";
	
	/**
	 * The Constant SAVE_FTP_SERVER_DETAILS.
	 */
	public static final String SAVE_FTP_SERVER_DETAILS = "insert into ftp_server_details (screen_name, host_address, username, password, destdir, file_prefix, transferType, fingerprint) VALUES (?,?,?,?,?,?,?,?)";
	
	/**
	 * The Constant UPDATE_ARCHIVE_PREFERENCES.
	 */
	public static final String UPDATE_ARCHIVE_PREFERENCES = "insert into archive_trimmed_at (module_name, trimmed_at) VALUES (?,?)";

	/**
	 * The Constant GET_SYSLOG_DATA.
	 */
	public static final String GET_SYSLOG_DATA = " select * from syslog_messages where received_at < ?";
	
	/**
	 * The Constant UPDATE_JOB_DESCRIPTION.
	 */
	public static final String UPDATE_JOB_DESCRIPTION = "update pari_job_details set DESCRIPTION=? where job_name=? AND job_group=?";

   /**
    * The Constant CUSTOMER_UPDATE.
    */
   static final String CUSTOMER_UPDATE = "UPDATE customers SET " +
   " customer_name=?, contact_name=?, contact_email=?,  contact_phone=?, wing_login=?, security_key=? WHERE " + " customer_id=?";
   
   /**
    * The Constant CUSTOMER_INSERT.
    */
   static final String CUSTOMER_INSERT = "INSERT INTO customers " +
   "(customer_id, customer_name, contact_name, contact_email, contact_phone, wing_login, security_key, creator_id, creator_name) " + " VALUES (?,?,?,?,?,?,?,?,?)";
   
   /**
    * The Constant INSERT_CUST_PCB_IMPORT.
    */
   public static final String INSERT_CUST_PCB_IMPORT = "insert into cust_pcb_import (customer_id, upload_time, remarks, instance_name) VALUES (?, ?, ?, ?)";
   
   /**
    * The Constant INSERT_CUST_PCB_IMPORT_DET.
    */
   public static final String INSERT_CUST_PCB_IMPORT_DET = "insert into cust_pcb_import_det (customer_id, row_id, device_ip,  upload_status, remarks)  VALUES (?, ?, ?, ?, ?)";
   
   /**
    * The Constant UPDATE_CUST_PCB_IMPORT.
    */
   public static final String UPDATE_CUST_PCB_IMPORT = "update cust_pcb_import set remarks=? where customer_id=? AND row_id=?";
   
   /**
    * The Constant RETRIEVE_CUST_PCB_IMPORT_ROW_ID.
    */
   public static final String RETRIEVE_CUST_PCB_IMPORT_ROW_ID = "select row_id from cust_pcb_import where customer_id=? AND upload_time=?";
   
   /**
    * The Constant UPDATE_CATOS_VERSION.
    */
   public static final String UPDATE_CATOS_VERSION = "insert into catos_version (id, mcpsw_version, hardware_version) VALUES (?,?,?)";
   
   /**
    * The Constant INSERT_CATOS_SWITCH_MODULES.
    */
   public static final String INSERT_CATOS_SWITCH_MODULES = "insert into catos_switch_modules (id, module, model, port, serial, hw, fw, fw1, sw, sw1) VALUES (?,?,?,?,?,?,?,?,?,?)";

   /**
    * The Constant INSERT_REPORT_PROFILE_SUMMARY.
    */
   static final String INSERT_REPORT_PROFILE_SUMMARY = "insert into report_profile_summary (profile_id, profile_title, description, creator_id) VALUES (?,?,?,?)";
   
   /**
    * The Constant INSERT_REPORT_PROFILE_DETAILS.
    */
   static final String INSERT_REPORT_PROFILE_DETAILS = "insert into report_profile_details (profile_id, report_id, graph_id, columns, device_selection_type, selected_devices, report_descr, chart_descr, legend_type, creator_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
   
   /**
    * The Constant DELETE_REPORT_PROFILE_SUMMARY.
    */
   static final String DELETE_REPORT_PROFILE_SUMMARY = "delete from report_profile_summary";

   /**
    * The Constant INSERT_CUSTOMER_WING_SETTINGS.
    */
   public static final String INSERT_CUSTOMER_WING_SETTINGS = "INSERT INTO customer_wing_settings (customer_id, instance_name, row_id, settings )  VALUES (?,?,?,?)";
   
   /**
    * The Constant UPDATE_WING_SETTINGS_UPDATE_DETAILS_MODIFIED.
    */
   public static final String UPDATE_WING_SETTINGS_UPDATE_DETAILS_MODIFIED = "update wing_settings_update_details set modified_time=? where customer_id=? and instance_name=?";
   
   /**
    * The Constant INSERT_WING_SETTINGS_UPDATE_DETAILS_MODIFIED.
    */
   public static final String INSERT_WING_SETTINGS_UPDATE_DETAILS_MODIFIED = "insert into wing_settings_update_details (customer_id, instance_name, modified_time) VALUES (?,?,?)";
   
   /**
    * The Constant INSERT_WING_SETTINGS_UPDATE_DETAILS_UPLOAD.
    */
   public static final String INSERT_WING_SETTINGS_UPDATE_DETAILS_UPLOAD = "insert into wing_settings_update_details (customer_id, instance_name, upload_time) VALUES (?,?,?)";
   
   /**
    * The Constant UPDATE_WING_SETTINGS_UPDATE_DETAILS_UPLOAD.
    */
   public static final String UPDATE_WING_SETTINGS_UPDATE_DETAILS_UPLOAD = "update wing_settings_update_details set upload_time=? where customer_id=? and instance_name=?";
   
   /**
    * The Constant INSERT_CONFIG_IGNORE_STRINGS.
    */
   public static final String INSERT_CONFIG_IGNORE_STRINGS = "insert into config_ignore_strings (config_string) values (?)";
   
   /**
    * The Constant IOS_VERSION_DUMP_INSERT.
    */
   public static final String IOS_VERSION_DUMP_INSERT = "INSERT INTO version_dump (id, version_part, version) VALUES (?,?,?)";
   
   /**
    * The Constant INSERT_SHOW_COMMAND.
    */
   public static final String INSERT_SHOW_COMMAND = "INSERT INTO show_commands (device_id, cli, out_put, output_part) VALUES (?,?,?,?)";

   /**
    * The Constant INSERT_MAC_VENDORS.
    */
   static final String INSERT_MAC_VENDORS = "INSERT INTO mac_vendors (mac_prefix, vendor_name) values (?, ?)";

   /**
    * The Constant INSERT_DISCOVERED_DEVICE.
    */
   
   public static final String INSERT_DISCOVERED_DEVICE = "INSERT INTO discovered_devices (customer_id, instance_id, ipaddress, macaddress, hostname, snmp_object_id, description, device_family, product_family, product_model, os_name, os_version, vendor_name, is_managed, discovered_from, discovery_method, discovery_credential, current_state, discovery_time,primary_device_name,job_id,status,isPingable,isSnmpReachable,discoveryType,workingCred,discoveredBy) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
  
   /**
    * The Constant INSERT_MODULE_EOL_DB.
    */
   public static final String INSERT_MODULE_EOL_DB = "INSERT INTO module_eol_eox (fru, url, device_type, announcement_date, eos_date, eol_date, eocr_date, eoe_date)  values(?,?,?,?,?,?,?,?)";
   
   /**
    * The Constant INSERT_SHOW_DIAG_MODULE.
    */
   public static final String INSERT_SHOW_DIAG_MODULE = "INSERT INTO DEVICE_MODULE_INFO (DEVICE_ID, MODULE_NAME, MODULE_DESCR, PID, VID, SERIALNUM) VALUES (?,?,?,?,?,?)";
   
   /**
    * The Constant INSERT_VOIP_PHONE.
    */
   public static final String INSERT_VOIP_PHONE = "INSERT INTO VOIP_PHONES (customer_id, instance_id, ip_address, phone_model, phone_vendor, phone_mac_address, device_id,switch_intf_name,phone_serial_num,phone_user_name,phone_extension) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
   
   /**
    * The Constant INSERT_USER_DOMAINS.
    */
   static final String INSERT_USER_DOMAINS = "INSERT INTO user_domains " +
   "(user_id, customer_id) VALUES (?,?)";

   /**
    * The Constant WING_INSTANCE_UPDATE.
    */
   static final String WING_INSTANCE_UPDATE = "UPDATE wing_instance SET " +
   " instance_name=?, contact_name=?, contact_email=?,  contact_phone=?, location=?, description=? WHERE " + " instance_id=?";
   
   /**
    * The Constant WING_INSTANCE_INSERT.
    */
   static final String WING_INSTANCE_INSERT = "INSERT INTO wing_instance " +
   "(instance_id, instance_name, contact_name, contact_email, contact_phone, location, description) " + " VALUES (?,?,?,?,?,?,?)";
    
   /**
    * The Constant INSERT_SNMP_ENT_MIB.
    */
   public static final String INSERT_SNMP_ENT_MIB = "insert into snmp_entity_mib  (device_id,sys_oid,entPhysicalDescr,entPhysicalClass,entPhysicalName,entPhysicalSerialNum,entPhysicalModelName, entPhysicalVendorType, entPhysicalHardwareRev) values (?,?,?,?,?,?,?,?,?)";
  
   /**
    * The Constant INSERT_CUSTOMER_INSTANCE.
    */
   static final String INSERT_CUSTOMER_INSTANCE = "INSERT INTO customer_instance  (customer_id, instance_id) VALUES (?,?)";
   
   /**
    * The Constant UPDATE_DB_DETAILS.
    */
   public static final String UPDATE_DB_DETAILS = "INSERT INTO db_conn_details  (host, port, db_name, user_name, password) VALUES (?,?,?,?,?)";

//   public static final String INSERT_DISC_JOB_STATUS = "INSERT INTO discovery_job_status_msgs  (job_id, msg_number, ipaddress, hostname, device_type, msg_status, message) VALUES (?,?,?,?,?,?,?)";
//   public static final String UPDATE_DISC_JOB_STATUS = "UPDATE  discovery_job_status_msgs set hostname=?, device_type=?, message=?, msg_status=? where msg_number=?";

   /**
     * The Constant INSERT_DISC_JOB_RUN_STATUS.
    */
   public static final String INSERT_DISC_JOB_RUN_STATUS = "INSERT INTO discovery_job_run_status  (job_id,  ipaddress, bucket, message, discovery_method, neihbor_ip) VALUES (?,?,?,?,?,?)";
   
   /**
    * The Constant UPDATE_DISC_JOB_RUN_STATUS.
    */
   public static final String UPDATE_DISC_JOB_RUN_STATUS = "UPDATE discovery_job_run_status set bucket=?, message=?, neighbor_ip=? where job_id=? and ipaddress=?";
   
   /**
    * The Constant INSERT_INTO_STANDBY_IP.
    */
   public static final String INSERT_INTO_STANDBY_IP = "INSERT INTO standby_to_id   (standby_ip, id) VALUES (?,?)";
   
   /**
    * The Constant UPDATE_QRTZ_JOB_CREATOR_DETAILS.
    */
   public static final String UPDATE_QRTZ_JOB_CREATOR_DETAILS  = "INSERT INTO pari_job_addl_details (job_name, job_group, creator_id, creator_login, created_time,workflow_id, parent_jobid, service_name) VALUES (?,?,?,?,?,?,?,?)";
   
   /**
    * The Constant UPDATE_QRTZ_JOB_MODIFIER_DETAILS.
    */
   public static final String UPDATE_QRTZ_JOB_MODIFIER_DETAILS = "UPDATE  pari_job_addl_details set modifier_id=?,  modifier_login=?, modified_time=?, service_name=? where job_id=?";
   
   /**
    * The Constant INSERT_NODE_ADDTL_DETAILS.
    */
   public static final String INSERT_NODE_ADDTL_DETAILS = "INSERT INTO nodes_addtl_details (id, sysoid, sysDescr, domainname, syslocation, hwrevision, tan, tanrevision, knownips, devicesource, sysServices, sysContact, sysName)   values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
   
   /**
    * The Constant UPDATE_NODE_ADDTL_DETAILS.
    */
   public static final String UPDATE_NODE_ADDTL_DETAILS = "UPDATE  nodes_addtl_details set sysoid=?, sysDescr=?, domainname=?, syslocation=?, hwrevision=?, tan=?, tanrevision=?, knownips=?, devicesource=?, sysServices=?, sysContact=?, sysName=?  where id=?";

	/**
	 * The Constant INSERT_VERSION_ADDL_DETAILS.
	 */
	static final String INSERT_VERSION_ADDL_DETAILS = "INSERT INTO version_addl_details (id, family) VALUES (?,?)";
	
	
	/**
	 * The Constant INSERT_VERSION_ADDL_DETAILS.
	 */
	static final String UPDATE_VERSION_ADDL_DETAILS = "UPDATE version_addl_details set family=? where id=?";
	
	
	static final String INSERT_PAUSED_JOB_DETAILS = "INSERT INTO paused_job_details (jobId, jobRunId, login, pausedTime) VALUES (?,?,?,?)";

	public static final String UPDATE_USER_SECRET_QUESTIONS = "insert into user_secret_question_map (user_id, secret_question, secret_answer_md5) VALUES (?,?,?)";
	
	/*** Alert Configuration  **/

	public static final String SELECT_ALERT_CONFIGURATION ="select * from alert_configuration where module=?";
	public static final String INSERT_ALERT_CONFIGURATION ="insert into alert_configuration (module,protocol_list,percentage,instance_id) VALUES (?,?,?,?)";
	public static final String UPDATE_ALERT_CONFIGURATION ="update alert_configuration set protocol_list= ? , percentage = ? where module= ? and instance_id=?";
	public static final String DELETE_ALERT_CONFIGURATION ="delete from alert_configuration where module = ?";
	public static final String SELECT_ALL_ALERT_CONFIGURATION ="select * from alert_configuration";
	public static final String DELETE_ALL_ALERT_CONFIGURATION="delete from alert_configuration";

	/*** Event Configuration  **/

	public static final String SELECT_EVENT_TRANSACTION_1="select * from event_transaction where instance_id=?";

	public static final String SELECT_EVENT_TRANSACTION_2="select * from event_transaction where event_id=?";


	public static final String INSERT_EVENT_TRANSACTION_1="insert into event_transaction (event_id, module,event_def_id, subject, time_of_event, severity,count,message,status,job_details,additional_info,description,instance_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

			//DELETE statements for table event_transaction
			public static final String DELETE_EVENT_TRANSACTION_1 = "delete from event_transaction where event_id = ? and instance_id=?";
		        public static final String DELETE_EVENT_TRANSACTION_2 = "delete from event_transaction where time_of_event < ?";
			public static final String DELETE_EVENT_TRANSACTION_3=  "delete from event_transaction where instance_id=?";


			/**************************************************
			 * TABLE NAME : SUBSCRIPTION_DETAILS		 	  *
			 **************************************************/

			//SELECT statements for table subscription_details
					
	public static final String SELECT_SUBSCRIPTION_DETAILS_1 ="select * from subscription_details";

					//INSERT statements for table subscription_details
					
	public static final String INSERT_SUBSCRIPTION_DETAILS_1 ="insert into subscription_details(module,notification_enabled,notification_type,emails,instance_id) VALUES (?,?,?,?,?)";

					//UPDATE statements for table subscription_details
					
	public static final String UPDATE_SUBSCRIPTION_DETAILS_1 =  "update subscription_details set emails=? where module=? and instance_id=?";
	public static final String UPDATE_SUBSCRIPTION_DETAILS_2 ="update subscription_details set notification_enabled=?,notification_type=?,emails=? where module=? and instance_id=?";

					//DELETE statements for table subscription_details
					
	public static final String DELETE_SUBSCRIPTION_DETAILS_1 =  "delete from subscription_details where module=? and instance_id=?";
					//DELETE statements for table subscription_details
					public static final String DELETE_SUBSCRIPTION_DETAILS_2 = "delete from subscription_details where instance_id=?";
					
    public static final String	SELECT_SMTP_SETTINGS_1 =  "select * from smtp_settings";
    

	//INSERT statements for table smtp_settings
    public static final String INSERT_SMTP_SETTINGS_1 = "insert into smtp_settings (smtp_host, smtp_port, smtp_login, smtp_password, mail_from, use_ssl, emails, instance_id)  VALUES (?,?,?,?,?,?,?,?)";
    public static final String INSERT_SMTP_SETTINGS_2 =  "insert into smtp_settings(smtp_host, smtp_port, smtp_login, smtp_password, mail_from, emails, instance_id) values (?,?,?,?,?,?,?)";
  //UPDATE statements for table smtp_settings
    public static final String UPDATE_SMTP_SETTINGS_1 = "update smtp_settings set smtp_port=?,smtp_login=?,smtp_password=?,mail_from=?,emails=?,smtp_host=? where instance_id=?";

  		//DELETE statements for table smtp_settings
    public static final String DELETE_SMTP_SETTINGS_1 = "delete from smtp_settings";
    public static final String DELETE_SMTP_SETTINGS_2 = "delete from smtp_settings where instance_id =?";
    
    public static final String SELECT_DAV_DATA_SUCCESS_COUNT = "SELECT protocol, count(*) FROM dav_data where job_id = ? AND job_run_id = ?  and status = ?  group by protocol";
    public static final String SELECT_DAV_DATA_ALL_COUNT = "SELECT protocol, count(*) FROM dav_data where job_id = ? AND job_run_id = ? group by protocol";
    static final String INSERT_CLICHANNEL_DEFINITIONS = "INSERT INTO channel_definitions" +
    		"(channel_id, channel_defs, more_prompts_defs, enablemode_defs, clearterminal_length_defs, after_login_command, replaceEscChar, clearline_defs, controlchar, priority,terminalSettings, other_prompts_defs,usePariPatentEndofComamnd,before_login_defs,retry_interval,algorithm_preferences,DynamicDevicePrompt) " +
    		" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    static final String UPDATE_CLICHANNEL_DEFINITIONS = "UPDATE channel_definitions set " +
    		"channel_defs=?, more_prompts_defs=?, enablemode_defs=?, clearterminal_length_defs=?, after_login_command=?, replaceEscChar=?, clearline_defs=?, controlchar=?, priority=?, terminalSettings=?, other_prompts_defs=?,usePariPatentEndofComamnd=?,before_login_defs=?,retry_interval=?,algorithm_preferences=?,DynamicDevicePrompt=? " +
    		"where channel_id=?";
    
    static final String INSERT_TRANSPORT_DEFINITIONS = "INSERT INTO transport_channel " +
    		"(channel_id, match_definitions, priority) " +
    		" VALUES (?,?,?)";
    
    static final String INSERT_TRANSPORTCHANNEL_DEFINITIONS = "INSERT INTO transport_channel_definitions " +
    		"(channel_id, type, channel_definitions) " +
    		" VALUES (?,?,?)";
    
    static final String UPDATE_TRANSPORT_DEFINITIONS = "UPDATE transport_channel set " +
    		"match_definitions=?, priority=? where channel_id=?";
    
    public static final String SELECT_TRANSPORT_DEFINITIONS = "select channel_id, match_definitions, priority from transport_channel ORDER BY priority DESC";
    
    public static final String SELECT_TRANSPORTCHANNEL_DEFINITIONS = "select type, channel_definitions from transport_channel_definitions where channel_id = ?";
    
    public static final String DELETE_TRANSPORT_CHANNEL_DEFINITIONS = "DELETE from transport_channel_definitions where channel_id=? and type=?";
    
    static final String DELETE_TRANSPORTCHANNEL_DEFINITIONS = "delete from transport_channel where channel_id = ?";
    
    static final String DELETE_CLICHANNEL_DEFINITIONS = "delete from channel_definitions where channel_id = ?";
    
    public static final String SELECT_CLI_DISCOVERY_RULES = "select rule_id,vendor,os_type_rules,os_version_rules,serial_number_rules,product_Model_Rules,host_name_rules from cli_discovery_rules_table";
    
    public static final String INSERT_CLI_DISCOVERY_RULES = "INSERT INTO cli_discovery_rules_table(rule_id,vendor,os_type_rules,os_version_rules,serial_number_rules,product_Model_Rules,host_name_rules) VALUES(?,?,?,?,?,?,?)";
    
    public static final String UPDATE_CLI_DISCOVERY_RULES = "update cli_discovery_rules_table set vendor=?, os_type_rules=?,os_version_rules=?,serial_number_rules=?,product_Model_Rules=?,host_name_rules=? where rule_id=?";
    
    public     static final String DELETE_CLI_DISCOVERY_RULES = "delete from cli_discovery_rules_table where rule_id = ?";
    
	public static final String SELECT_DEVICE_CRED ="select password,enable_password,cred_entry_id,rd_community,wr_community, v3_auth_passphrase, v3_priv_passphrase from system_credentials";
    public static final String UPDATE_DEVICE_CRED = "UPDATE system_credentials SET " + " enable_password=?, password=?, rd_community=?, wr_community=?, v3_auth_passphrase=?, v3_priv_passphrase=? WHERE " +" cred_entry_id=?";    
    public static final String SELECT_JUMP_CRED   = "select password,cred_entry_id from jump_server_credentials";
    public static final String UPDATE_JUMP_CRED   = "UPDATE jump_server_credentials SET " + " password=? WHERE " + " cred_entry_id=?";

    public static final String SELECT_MOD_CRED 	  = "select password,enable_password, cred_set_name from module_credentials";
    public static final String UPDATE_MOD_CRED	  = "update module_credentials set" + " password=?, enable_password=? where " + "cred_set_name =?";
    
    public static final String SELECT_CLI_COMMANDS = "select priority,cliCommand  from cli_discovery_commands_table ORDER BY priority";
    public static final String INSERT_CLI_COMMANDS= "INSERT INTO cli_discovery_commands_table(priority,cliCommand) VALUES(?,?)";
    public static final String DELETE_CLI_COMMAND = "delete from cli_discovery_commands_table where cliCommand =?";
    public static final String UPDATE_CLI_COMMAND = "update cli_discovery_commands_table set priority=?  where cliCommand=?";
    
    public static final String SELECT_SNMP_RULES = "select os_name,check_snmp_status_commands,snmpstatus_pattern,enable_snmp_command,snmp_ro_string from snmpconfigpush_definitions;";
    public static final String INSERT_SNMP_RULES = "insert into snmpconfigpush_definitions(os_name,check_snmp_status_commands,snmpstatus_pattern,enable_snmp_command,snmp_ro_string) values (?,?,?,?,?);";
    public static final String DELETE_SNMP_COMMAND = "delete from snmpconfigpush_definitions where os_name=?";
    
	public static final String SELECT_ADMIN_EMAIL = "select email from user_details where group_id=?";
	
    /**
     * The Constant INSERT_DISCOVERY_JOB_MONITER.
     */
    
    public static final String INSERT_DISCOVERY_JOB_MONITER = "INSERT INTO discovery_job_moniter (jobid, jobrunid, ipaddress, snmp_protocol, status) VALUES (?,?,?,?,?)";
    public static final String INSERT_DEVICE_PROMPT_JOB_MONITER = "INSERT INTO device_prompt_job_moniter (jobid, jobrunid, ipaddress, status) VALUES (?,?,?,?)";
    public static final String GET_ALL_JOB_IDS_FOR_A_SCHEDULED_INV_GROUP = "select job_id from pari_job_addl_details where job_group= 'InventoryJobGroup'";
    public static final String UPDATE_SERVICENAME_FOR_JOB = "update pari_job_addl_details set service_name=? where job_id=?";
    public static final String INSERT_INVENTORY_JOB_MONITER = "INSERT INTO inventory_job_moniter (jobid, jobrunid, ipaddress, status) VALUES (?,?,?,?)";
    public static final String UPDATE_PAUSED_DURATION_FOR_A_JOB = "update pari_job_runs set paused_duration=? where jobid=? and job_runid=?";
    public static final String DELETE_DISCOVERY_JOB_MONITER_FOR_JOB = "delete from discovery_job_moniter where  ipaddress=? and jobid=? and jobrunid=? and snmp_protocol=?";

	public static final String DELETE_INVENTORY_JOB_MONITER_FOR_JOB = "delete from inventory_job_moniter where ipaddress=? and jobid=? and jobrunid=?";
    
}

