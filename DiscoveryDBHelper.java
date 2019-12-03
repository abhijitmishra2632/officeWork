/**
 * Copyright (c) 2005 - 2007 Pari Networks, Inc.  All Rights Reserved.
 *
 * This software is the proprietary information of Pari Networks, Inc.
 *
 */

package com.pari.nm.utils.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.cisco.web.mw.core.PagedTreeFilterCfg;
import com.cisco.web.mw.core.PagingContext;
import com.cisco.web.mw.core.SortingContext;
import com.pari.base.so.DiscoveredDevice;
import com.pari.base.so.NetworkNode;
import com.pari.extif.jms.Messenger;
import com.pari.logger.LoggerNames;
import com.pari.logger.PariLogger;
import com.pari.logger.PariLoggerFactory;
import com.pari.nm.extif.events.CspcEventTopics;
import com.pari.nm.gui.guiservices.PariMsgException;
import com.pari.nm.modules.jobs.DeviceJobMoniter;
import com.pari.nm.modules.session.NetworkNodeCache;
import com.pari.nm.utils.LittleUtils;
import com.pari.nm.utils.ProtocolType;
import com.pari.nm.utils.VendorAliasMapper;
import com.pari.server.IPariMain;
import com.pari.services.def.ServiceDescriptor;

/**
 * The Class DiscoveryDBHelper.
 */
public class DiscoveryDBHelper
{
   
   /**
    * The logger.
    */
	private static final PariLogger logger = PariLoggerFactory.getLogger(LoggerNames.DISCOVERY);
   /**
    * Insert mac2 vendors.
    */
   public static void insertMac2Vendors()
   {
      int macVendorsCount = getMac2VendorCount();
      if (  macVendorsCount > 0 )
      {
         logger.info("db already contains mac vendors : " + macVendorsCount);
         return;
      }

      logger.debug("Loading mac address vendors");

      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
            "device-types/mac2vendors.txt");
      if ( inputStream == null )
      {
         logger.warn("Unable to load mac-address to vendor-name mapping list.");
         return;
      }
      
      Connection        c              = null;
      PreparedStatement ps             = null;
      InputStreamReader inReader       = null;
      BufferedReader    bufferedReader = null;
      try
      {
         inReader       = new InputStreamReader(inputStream);
         bufferedReader = new BufferedReader(inReader);

         String line = null;

         c = DBHelper.getConnection();
         c.setAutoCommit(false);
         ps = c.prepareStatement(DBHelperConstants.INSERT_MAC_VENDORS);

         while ( (line = bufferedReader.readLine()) != null )
         {
            if ( line.trim().length() > 0 )
            {
               String[] tokens = line.split(":");
               if ( tokens.length == 2 )
               {
                  ps.setString(1, tokens[0]);
                  ps.setString(2, VendorAliasMapper.getInstance().getAlias(tokens[1]));
   
                  try
                  {
                     ps.executeUpdate();
                  }
                  catch ( Exception e )
                  {
                     logger.warn("Exception while inserting mac 2 vendor data");
                  }
               }
            }
         }
      }
      catch ( Exception e )
      {
         logger.warn("Exception while inserting mac2vendor data.", e);
      }
      finally
      {
         try
         {
            ps.close();
         }
         catch ( Exception ee ) 
         {
            logger.trace("Error while closing prepared statement", ee);
         }

         try
         {
            c.commit();
         }
         catch ( Exception e1 ) 
         {
            logger.warn("Error while committing", e1);
         }

         try
         {
            c.setAutoCommit(true);
         }
         catch ( Exception ee ) 
         {
            logger.trace("Error while setting auto commit", ee);
         }
         DBHelper.releaseConnection(c);
      }
   }

   /**
    * Gets the mac2 vendor count.
    *
    * @return the mac2 vendor count
    */
   public static int getMac2VendorCount()
   {
      ResultSet rs = null;
      try
      {
         rs = DBHelper.executeQuery("select count(*) from mac_vendors");
         if ( rs.next() )
         {
            return rs.getInt(1);
         }
      }
      catch ( Exception ex )
      {
         logger.warn("Exception while getting mac2vendor information.", ex);
      }
      finally
      {
         try
         {
            rs.close();
         }
         catch ( Exception ee ) 
         {
            logger.trace("Error while closing the result set", ee);
         }
      }

      return -1;
   }
   
   /**
    * Gets the mac2 vendor name.
    *
    * @param macAddr the mac addr
    * @return the mac2 vendor name
    */
   public static String getMac2VendorName(String macAddr) 
   {
      ResultSet rs = null;
      try
      {
         String query = "select VENDOR_NAME from mac_vendors where MAC_PREFIX =  ? ";
		 Object[] values = new Object[] {macAddr};
		 rs = DBHelper.executeQuery(query, values);
         if ( rs.next() )
         {
            return rs.getString(1);
         }
      }
      catch ( Exception ex )
      {
         logger.warn("Exception while getting mac2vendor information.", ex);
      }
      finally
      {
         try
         {
            rs.close();
         }
         catch ( Exception ee ) 
         {
            logger.trace("Error while closing the result set", ee);
         }
      }

      return null;
   }
   
   /**
    * Removes the all discovered devices.
    *
    * @param customerId the customer id
    * @throws Exception the exception
    */
   public static void removeAllDiscoveredDevices(int customerId) throws Exception
   {
      try 
      {
         DBHelper.executeUpdate("delete from discovered_devices where customer_id=" + customerId);
      }
      catch (Exception ex)
      {
         logger.warn();
      }
   }

   /**
    * Insert discovered device.
    *
    * @param discDevice the disc device
    */
   public static void insertDiscoveredDevice(DiscoveredDevice discDevice)
   {
      Connection c = null;
      PreparedStatement ps = null;
      try
      {
    	 DBHelper.executeUpdate("delete from discovered_devices where  ipaddress='" + discDevice.getIpAddress() + "'and customer_id=" + discDevice.getCustomer_id() + " and instance_id=" + discDevice.getInstanceId());
          
         logger.debug(" delete from discovered_devices completed sucessfully");  

         c = DBHelper.getConnection();
         ps = c.prepareStatement(DBHelperConstants.INSERT_DISCOVERED_DEVICE);
         ps.setInt(1, discDevice.getCustomer_id());
         ps.setInt(2, discDevice.getInstanceId());
         ps.setString(3, discDevice.getIpAddress());
         ps.setString(4, discDevice.getMacAddress());
         ps.setString(5, discDevice.getNodeName());
         ps.setString(6, discDevice.getSnmpObjectId());
         ps.setString(7, discDevice.getDescription());
         ps.setString(8, discDevice.getDevice_family());
         ps.setString(9, discDevice.getProduct_family());
         ps.setString(10, discDevice.getProduct_model());
         ps.setString(11, discDevice.getOSName());
         ps.setString(12, discDevice.getOSVersion());
         ps.setString(13, discDevice.getVendor_name());
         if (discDevice.isManaged())
         {
            ps.setString(14, "Yes");
         }
         else
         {
            if (NetworkNodeCache.getInstance().getNode(discDevice.getIpAddress()) != null && (NetworkNodeCache.getInstance().getNode(discDevice.getIpAddress()).equals(discDevice.getPrimaryDeviceName())))
            {
               ps.setString(14, "Yes");
            }
            else
            {
               ps.setString(14, "No");
            }
         }
         ps.setString(15, discDevice.getDiscovered_from());
         ps.setString(16, discDevice.getDiscovery_method());
         ps.setString(17, discDevice.getDiscovery_credential());
        // ps.setString(18, null);
         ps.setString(18, discDevice.getCurrentState());
         ps.setTimestamp(19, discDevice.getDiscovery_time() == null? (new Timestamp(System.currentTimeMillis())): (new Timestamp(Long.parseLong(discDevice.getDiscovery_time()))));
         ps.setString(20, discDevice.getPrimaryDeviceName());
         ps.setInt(21, discDevice.getJobId());
         ps.setString(22,discDevice.getStatus());
         ps.setString(23,discDevice.isPingStatus());
         ps.setString(24,discDevice.isSnmpStatus());
         ps.setString(25, discDevice.getManagementType());
         ps.setString(26,discDevice.getWorkingCred());
         ps.setString(27, discDevice.getDiscoveryProtocol());
         ps.executeUpdate();
      }
      catch (Exception ee)
      {
    	  logger.error(ee);
         logger.warn("Error while inserting into discovered devices", ee);
      }
      finally
      {
         try
         {
            ps.close();
         }
         catch (Exception ee)
         {
            logger.trace("Error while closing the prepared statement", ee);
         }
         try
         {
            DBHelper.releaseConnection(c);
         }
         catch (Exception ee)
         {
            logger.trace("Error while releasing the connection", ee);
         }
      }
   }
   
   
   /**
    * Insert discovered job Moniter
 * @param status 
 * @param string 
    *
    */
   public static void insertDiscoveryJobMoniter(int jobId, int jobRunId, ArrayList<DeviceJobMoniter> deviceStatus) {
	   Connection c = null;
	   final int BATCH_SIZE = 1000;
	   final int DATA_SIZE = deviceStatus.size();
	   List<Object[]> dataObjectsDeleteQuery = new ArrayList<>();
	   List<Object[]> dataObjectsInsertQuery = new ArrayList<>();
	   try {
		   c = DBHelper.getConnection();
		   for(DeviceJobMoniter djm : deviceStatus){
			   dataObjectsDeleteQuery.add(new Object[]{djm.getIpAddress(), jobId, jobRunId, djm.getCommandOrProtocol()});
			   dataObjectsInsertQuery.add(new Object[]{jobId, jobRunId, djm.getIpAddress(), djm.getCommandOrProtocol(), djm.isStatus()});
		   }
		   DBHelper.executeQueryInBatch(c, DBHelperConstants.DELETE_DISCOVERY_JOB_MONITER_FOR_JOB, BATCH_SIZE, DATA_SIZE, dataObjectsDeleteQuery);
		   DBHelper.executeQueryInBatch(c, DBHelperConstants.INSERT_DISCOVERY_JOB_MONITER, BATCH_SIZE, DATA_SIZE, dataObjectsInsertQuery);
	   } catch (Exception ee) {
		   logger.error(ee);
		   logger.warn("Error while inserting into discovery_job_moniter", ee);
	   } finally {
		   try {
			   c.setAutoCommit(true);
			   DBHelper.releaseConnection(c);
		   } catch (Exception ee) {
			   logger.trace("Error while releasing the connection", ee);
		   }
	   }
   }
   
   
   /**
    * Insert discovered job Moniter
    *
    */
   public static void removeDiscoveryJobMoniter(int jobId, int jobRunId)
   {
      try
      {
    	 DBHelper.executeUpdate("delete from discovery_job_moniter where jobid=" + jobId + " and jobrunid=" + jobRunId);
          
         logger.debug(" delete from discovery_job_moniter completed sucessfully");  
      }
      catch (Exception ee)
      {
    	  logger.error(ee);
         logger.warn("Error while inserting into discovery_job_moniter", ee);
      }
   }
   
   
	public static ArrayList<String> getDiscoveredDeviceForJobID(int jobId, int jobRunId, String protocol) {
		ResultSet rs = null;
		ArrayList<String> ipaddress = null;
		try {
			rs = DBHelper.executeQuery(
					"select ipaddress from discovery_job_moniter where jobid=" + jobId + " and jobrunid=" + jobRunId + " and snmp_protocol = '" + protocol + "'");

			logger.debug(" select from discovery_job_moniter completed sucessfully + Resume call");
			ipaddress = new ArrayList<String>();
			while ( rs!=null && rs.next()) {
				ipaddress.add(rs.getString("ipaddress"));
			
			}
		} catch (Exception ee) {
			logger.error("Error while fetching the IP Address for already discovered device" + ee);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {

			}
		}
		return ipaddress;
	}
   
   
   
	public static ArrayList<String> getDiscoveredDeviceForFallbackProtocol(int jobId, int jobRunId, String protocol) {
		ResultSet rs = null;
		ArrayList<String> discoveredDevice = null;

		try {
			rs = DBHelper.executeQuery("select ipaddress from discovery_job_moniter where jobid=" + jobId
					+ " and jobrunid=" + jobRunId + " and status = " + true);

			logger.debug(" select from discovery_job_moniter completed sucessfully + Resume call");
			discoveredDevice = new ArrayList<String>();
			while (rs != null && rs.next()) {
				discoveredDevice.add(rs.getString("ipaddress"));

			}
		} catch (Exception ee) {
			logger.error("Error while fetching the IP Address for already discovered device" + ee);
		} finally {
			try {
				rs.close();
			} catch (Exception e) {

			}
		}
		return discoveredDevice;
	}
   
   
   
   
   
   
   /**
    * Save module config parameters.
    *
    * @param moduleName the module name
    * @param settingsName the settings name
    * @param settingsValue the settings value
    * @throws Exception the exception
    */
   public static final void saveModuleConfigParameters(String moduleName, String settingsName, String settingsValue) throws Exception
   {
	      Connection        c  = null;
	      PreparedStatement ps = null;
	      PreparedStatement ps1 = null;
	      
	      String deleteSql = "delete from module_config_params where module_name = ? and preference_name = ? ";
	      String insertSql = "insert into module_config_params (module_name, preference_name, preference_value) values (?, ?, ?)";

	      try
	      {
	         c = DBHelper.getConnection();
	         c.setAutoCommit(false);

	         ps1 = c.prepareStatement(deleteSql);
	         ps1.setString(1, moduleName);
	         ps1.setString(2, settingsName);
	         ps1.executeUpdate();
	         
	         if ( settingsValue != null )
	         {
	            ps = c.prepareStatement(insertSql);
	   
	            ps.setString(1, moduleName);
	            ps.setString(2, settingsName);
	            ps.setString(3, settingsValue);
	            ps.executeUpdate();
	         }
	   
	         c.commit();
	         
	         Messenger.getInstance().publishToTopic(CspcEventTopics.CSPC_CONFIG_PARAMS_UPDATED, "");
	      }
	      catch ( Exception ee )
	      {
	         try
	         {
	            if (c!=null) c.rollback();
	         }
	         catch ( Exception sqlEx ) 
	         {
	            logger.debug("Error while rollingback the data", sqlEx);
	         }
	         logger.warn("Error while saving config parameters for the module :" + moduleName, ee);
	         throw new PariMsgException("TBD", "Error while saving module config parameters");
	         
	      }
	      finally
	      {
	         try
	         {
	            c.setAutoCommit(true);
	         }
	         catch ( Exception e )
	         {
	            logger.debug("Error while Setting auto commit", e);
	         }
	         try{
	        	 if(ps!=null){
	        		 ps.close();
	        	 }  
	        	 if(ps1!=null){
	        		 ps1.close();
	        	 }
	         }catch ( Exception e )
	         {
	             logger.debug("Error while closing Prepared statement", e);
	          }

	         DBConnectionFactory.getInstance().releaseConnection(c);
	      }
	   }
   
   /**
    * Load module config parameters.
    *
    * @param moduleName the module name
    * @param preferenceName the preference name
    * @return the string
    * @throws Exception the exception
    */
   public static final String loadModuleConfigParameters(String moduleName, String preferenceName) throws Exception
   {
      String query = "select preference_value from module_config_params where module_name = ?  and preference_name = ? ";
      ResultSet rs = null;
	  Object[] values = new Object[] {moduleName,preferenceName};

      try
      {
         rs = DBHelper.executeQuery(query, values);
         if ( rs.next() )
         {
            return rs.getString("preference_value");
         }
      }
      catch ( Exception ee )
      {
         logger.warn("Error while loading config parameters for the module :" + moduleName, ee);
         throw new PariMsgException("TBD", "Error while loading module config parameters");
      }
      finally
      {
         try
         {
            rs.close();
         }
         catch ( Exception ee )
         {
            logger.debug("Error while closing result set", ee);
         }
      }
      return null;
   }
   
   /**
    * Gets the all module config parameters.
    *
    * @return the all module config parameters
    * @throws Exception the exception
    */
   public static final Map<String, Map<String, String>> getAllModuleConfigParameters() throws Exception
   {
      String query = "select module_name, preference_name, preference_value from module_config_params order by module_name, preference_name";
      ResultSet rs = null;

      Map<String, Map<String, String>> moduleMap = new LinkedHashMap<String, Map<String, String>>();
      try
      {
         rs = DBHelper.executeQuery(query);
         while ( rs.next() )
         {
            String moduleName = rs.getString("module_name");
            String prefName   = rs.getString("preference_name");
            String prefValue  = rs.getString("preference_value");
            
            Map<String, String> paramMap = moduleMap.get(moduleName);
            if ( paramMap == null )
            {
               paramMap = new LinkedHashMap<String, String>();
               moduleMap.put(moduleName, paramMap);
            }
            
            paramMap.put(prefName, prefValue);
         }
         
         return moduleMap;
      }
      catch ( Exception ee )
      {
         logger.warn("Error while loading config parameters ", ee);
         throw new PariMsgException("TBD", "Error while loading all module config parameters");
      }
      finally
      {
         try
         {
            rs.close();
         }
         catch ( Exception ee )
         {
            logger.debug("Error while closing result set", ee);
         }
      }
   }
   
   public static List<DiscoveredDevice> getUnreachableDevices(Map<String, Object> parameterMap) throws Exception {
	   StringBuilder query = new StringBuilder();
	   if(parameterMap != null && parameterMap.containsKey("UnreachableDevice") && parameterMap.get("UnreachableDevice").equals("Managed")){
		   query.append("select d.ipaddress AS `ipaddress`, d.hostname AS `hostname`, d.description AS `description`, d.discovery_time AS `discovery_time`, d.discoveryType AS `discoveryType` " +
		   			"from discovered_devices d,nodes n where d.ipaddress=n.ipaddress and n.isManaged='true' and d.current_state = 'Unreachable' ") ;  
	   }else{
		   query.append("select d.ipaddress AS `ipaddress`, d.hostname AS `hostname`, d.description AS `description`, d.discovery_time AS `discovery_time`, d.discoveryType AS `discoveryType` " +
				   "from discovered_devices d,nodes n where d.ipaddress=n.ipaddress and n.state='m' and d.current_state = 'Unreachable' ") ; 
	   }
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
		   String filterStr = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
	       	 if(!filterStr.isEmpty()){
	       		query.append(" AND "+filterStr);
	       	 }
   		}
	   
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.SORTINGCONTEXT)) {
			query.append(((SortingContext)parameterMap.get(IPariMain.SORTINGCONTEXT)).getSortingQuery((String)parameterMap.get(IPariMain.SERVICENAME)));
		}
		
		if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGINGCONTEXT)) {
			query.append(((PagingContext)parameterMap.get(IPariMain.PAGINGCONTEXT)).getPagingString());
		}
	   
	   ResultSet rs = null;
	   ArrayList<DiscoveredDevice> unreachableDeviceList = new ArrayList<DiscoveredDevice> ();
	   
	      try
	      {
	         rs = DBHelper.executeQuery(query.toString());
	         while ( rs.next() ) {
	        	 DiscoveredDevice device = new DiscoveredDevice();
	        	 device.setIpAddress(rs.getString("ipaddress"));
	        	 device.setNodeName(rs.getString("hostname"));
	        	 device.setDescription(rs.getString("description"));
	        	 device.setDiscovery_time(String.valueOf(rs.getTimestamp("discovery_time").getTime()));
	        	 String managedDevice = rs.getString("discoveryType");
	        	 boolean isManaged = managedDevice.equals("Managed")?true:false;
	        	 if(isManaged){
	        		// ServerProperties.getInstance().dumpToStdOut("Got the device  as "+isManaged);
	        	 }
	        	 device.setManaged(isManaged);
	        	 unreachableDeviceList.add(device);
	         }
	      } catch (Exception exp) {
	    	  logger.error("Exception " + exp);
	      } finally {
	    	  		DBHelper.releaseResource(rs);
	      }
	   return unreachableDeviceList;   
   }
   
   public static List<DiscoveredDevice> getDiscoveredDevices(Map<String, Object> parameterMap) throws Exception {
	   StringBuilder query = new StringBuilder();

	   query.append("select d.ipaddress AS `ipaddress`, `hostname` AS `hostname`, `description` AS `description`,`isPingable` AS `isPingable`,workingCred AS `workingCred` ,discoveredBy AS `discoveredBy`, n.id AS nodeId from discovered_devices d, nodes n where ((current_state<>'Unmanaged' OR current_state IS NULL) && (n.ipaddress=d.ipaddress) && n.state='m')") ; 

	  
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
		   String filterStr = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
	       	 if(!filterStr.isEmpty()){
	       		query.append(" AND "+filterStr);
	       	 }
   		}
	   
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.SORTINGCONTEXT)) {
			query.append(((SortingContext)parameterMap.get(IPariMain.SORTINGCONTEXT)).getSortingQuery((String)parameterMap.get(IPariMain.SERVICENAME)));
		}
		
		if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGINGCONTEXT)) {
			query.append(((PagingContext)parameterMap.get(IPariMain.PAGINGCONTEXT)).getPagingString());
		}
	   
	   ResultSet rs = null;
	   ArrayList<DiscoveredDevice> discoveredDeviceList = new ArrayList<DiscoveredDevice>();
	   
	      try
	      {
	    	 
	         rs = DBHelper.executeQuery(query.toString());
	         while ( rs.next() ) {
	        	 // Get the snmpdetails
	        	 DiscoveredDevice device = new DiscoveredDevice();
	        	 device.setIpAddress(rs.getString("ipaddress"));
	        	 device.setNodeName(rs.getString("hostname"));
	        	 String description = rs.getString("description");
	        	 String pingable= rs.getString("isPingable");
	        	 Integer nodeId= rs.getInt("nodeId");
	        	 device.setNodeId(nodeId);
	        	
	        	 StringBuffer finalStatus = new StringBuffer();
	        	 
	        	 finalStatus.append(description);
	        	 
	        	 if(pingable.equals("true")){
	        		 finalStatus.append(": Ping Reachable");
	        	 }
	        	 
	        	 
	        	 device.setDescription(finalStatus.toString());
	        	 
	        	 device.setWorkingCred(rs.getString("workingCred"));
	        	
	        	 device.setDiscoveryProtocol(rs.getString("discoveredBy"));
	        	 discoveredDeviceList.add(device);
	        	  
	         }
	        
	      } catch (Exception exp) {
	    	  logger.error("Exception " + exp);
	      } finally {
	    	  	  DBHelper.releaseResource(rs);
	      }
	   return discoveredDeviceList;   
   }
   
   
   
   
  

   
   public static int getDiscoveredDevicesCount(ServiceDescriptor descriptor, Map<String, Object> parameterMap) throws Exception {
	   StringBuilder query = new StringBuilder();
	   query.append("select * from discovered_devices d, nodes n where ((current_state<>'Unmanaged' OR current_state IS NULL) && (n.ipaddress=d.ipaddress) && n.state = 'm')"); 

	  if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
		  String filterStr = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
         	 if(!filterStr.isEmpty()){
         		query.append(" AND "+filterStr);
         	 }
	  }
	  
	   return DBHelper.getRowCountFrmQuery(query.toString());
	   
   }
   
   
  

   
   public static List<DiscoveredDevice> getDuplicateDevices(Map<String, Object> parameterMap) throws Exception {
	   StringBuilder query = new StringBuilder();
	   
	   query.append("select n.ipaddress as ipaddress, nep.prop_value as primary_device_name,duplicate_node_ipaddress as duplicate_ip,MANAGED_node_id as nodeid,TIME_LAST_ADDED as time_added from " +
	   		"node_ext_properties nep ,duplicate_nodes dn,nodes n where MANAGED_node_id=nep.id and nep.id = n.id  and prop_name='PRIMARY_DEVICE_NAME' and n.state = 'm'"); 
	  
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
				String filterQuery = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
				if(!filterQuery.isEmpty()){
					query.append(" AND "+filterQuery);
				}
			}
	   
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.SORTINGCONTEXT)) {
			query.append(((SortingContext)parameterMap.get(IPariMain.SORTINGCONTEXT)).getSortingQuery((String)parameterMap.get(IPariMain.SERVICENAME)));
		}
		
		if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGINGCONTEXT)) {
			query.append(((PagingContext)parameterMap.get(IPariMain.PAGINGCONTEXT)).getPagingString());
		}
	   
	   ResultSet rs = null;
	   ArrayList<DiscoveredDevice> duplicateDeviceList = new ArrayList<DiscoveredDevice>();
	   
	      try
	      {
	         rs = DBHelper.executeQuery(query.toString());
	         while ( rs.next() ) {
	        	 DiscoveredDevice device = new DiscoveredDevice();
	        	 String primary_device_name = rs.getString("primary_device_name");
	        	 String duplicate_device_ip = rs.getString("duplicate_ip");
	        	 int nodeId =  rs.getInt("nodeid");
	        	 String managedIpAddress = rs.getString("ipaddress");
	        	 device.setNodeId(nodeId);
	        	 device.setIpAddress(managedIpAddress);
	        	 device.setPrimaryDeviceName(primary_device_name);
	        	 device.setDuplicateIpAddress(duplicate_device_ip);
	        	 Timestamp sf =rs.getTimestamp("time_added");
	        	 String discoveryTime = String.valueOf(rs.getTimestamp("time_added").getTime());
	        	 device.setDiscovery_time(discoveryTime);
	        	 duplicateDeviceList.add(device);
	         }
	      } catch (Exception exp) {
	    	  logger.error("Exception " + exp);
	      } finally {
			  DBHelper.releaseResource(rs);
	      }
	   return duplicateDeviceList;   
   }
   
   public static Map<String, String>  populateUnreahableIPtoPrimaryNameMap(){
		Connection c = null;

		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> unreahableIPtoPrimaryNameMap = new HashMap<String,String>();
		try {
			c = DBHelper.getConnection();

			stmt = c.createStatement();
			rs = stmt.executeQuery("select ipaddress,primary_device_name from discovered_devices where current_state='Unreachable'");

			while (rs.next()) {
				String ipaddress = rs.getString("ipaddress");
				String primaryDeviceName = rs.getString("primary_device_name");	
				if(LittleUtils.isEmpty(ipaddress)) continue;
				unreahableIPtoPrimaryNameMap.put(ipaddress,LittleUtils.nonNullString(primaryDeviceName));

			}
		} catch (Exception e) {
			logger.error(e);
			//throw e;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
			if (c!=null) 
				DBHelper.releaseConnection(c);
		}

		return  unreahableIPtoPrimaryNameMap;
	}
   
   public static List<DiscoveredDevice> getNonSnmpDevices(ServiceDescriptor descriptor, Map parameterMap) throws Exception {
	   StringBuilder query = new StringBuilder();
	   query.append("select `ipaddress` AS `ipaddress`, `hostname` AS `hostname`, `device_family` AS `device_family`, `os_name` AS `os_name`, " +
	   		"`os_version` AS `os_version`, `vendor_name` AS `vendor_name`, discovery_time AS `discovery_time` " +
 			   			"from discovered_devices where discovery_method = 'Nmap' and current_state = 'Reachable'  and is_managed='No' "); 
	   
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
			String filterQuery = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
			if(!filterQuery.isEmpty()){
				query.append(" AND "+filterQuery);
			}
		}
	   
	   if (parameterMap != null && parameterMap.containsKey(IPariMain.SORTINGCONTEXT)) {
			query.append(((SortingContext)parameterMap.get(IPariMain.SORTINGCONTEXT)).getSortingQuery((String)parameterMap.get(IPariMain.SERVICENAME)));
		}
		
		if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGINGCONTEXT)) {
			query.append(((PagingContext)parameterMap.get(IPariMain.PAGINGCONTEXT)).getPagingString());
		}
		
	   ResultSet rs = null;
	   ArrayList<DiscoveredDevice> nonSnmpDeviceList = new ArrayList<DiscoveredDevice> ();
	   
	      try
	      {
	         rs = DBHelper.executeQuery(query.toString());
	         while ( rs.next() ) {
	        	 DiscoveredDevice device = new DiscoveredDevice();
	        	 device.setIpAddress(rs.getString("ipaddress"));
	        	 device.setNodeName(rs.getString("hostname"));
	        	 device.setDevice_family(rs.getString("device_family"));
	        	 device.setOsName(rs.getString("os_name"));;
	        	 device.setOsVersion(rs.getString("os_version"));
	        	 device.setVendor_name(rs.getString("vendor_name"));
	        	 device.setDiscovery_time(String.valueOf(rs.getTimestamp("discovery_time").getTime()));
	        	 nonSnmpDeviceList.add(device);
	         }
	      } catch (Exception exp) {
	    	  logger.error("Exception " + exp);
	      } finally {
			  DBHelper.releaseResource(rs);
	      }
	   return nonSnmpDeviceList;   
   }
   
   public static DiscoveredDevice getNonSnmpDevice(String ipaddress)  {
	   String query = "select `ipaddress` AS `ipaddress`, `hostname` AS `hostname`, `device_family` AS `device_family`, `os_name` AS `os_name`, " +
	   		"`os_version` AS `os_version`, `vendor_name` AS `vendor_name`, discovery_time AS `discovery_time` " +
 			   			"from discovered_devices where discovery_method = 'Nmap' and ipaddress = ? and current_state = 'Reachable' and is_managed='No' "; //Fix for CSCuw10367: Not able move devices discovered using nmap to managed devices using CLI

	   Object[] values = new Object[] {ipaddress};
	   ResultSet rs = null;
	   DiscoveredDevice nonSnmpDeviceList = new DiscoveredDevice();
	   
	      try
	      {
	    	 rs = DBHelper.executeQuery(query, values);;
	         while ( rs.next() ) {
	        	 DiscoveredDevice device = new DiscoveredDevice();
	        	 device.setIpAddress(rs.getString("ipaddress"));
	        	 device.setNodeName(rs.getString("hostname"));
	        	 device.setDevice_family(rs.getString("device_family"));
	        	 device.setOsName(rs.getString("os_name"));;
	        	 device.setOsVersion(rs.getString("os_version"));
	        	 device.setVendor_name(rs.getString("vendor_name"));
	        	 device.setDiscovery_time(String.valueOf(rs.getTimestamp("discovery_time").getTime()));
	        	 nonSnmpDeviceList = device;
	         }
	         rs.close();
	      } catch (Exception exp) {
	    	  logger.error("Exception " + exp);
	    	  return null;
	      } finally {
			  DBHelper.releaseResource(rs);
	      }
	   return nonSnmpDeviceList;   
   }
   
   public static void deleteDiscoveredDevice(String ipaddress) throws Exception {
	   try {
	         DBHelper.executeUpdate("delete from discovered_devices where ipaddress = '" + ipaddress + "'");
	   } catch (Exception ex) {
		   logger.warn();
	   }
   }
   
   
	
	
	/**
	    * Removes the all the Unreachable devices.
	    * @throws Exception the exception
	    */
	   public static void deleteAllUnreachableDevices() throws Exception
	   {
		   try 
		   {
			   DBHelper.executeUpdate("delete from discovered_devices where current_state in ('Unreachable')");
		   }
		   catch (Exception ex)
		   {
			   logger.warn();
		   } 
	   }
	   
	   /**
	    * Get the list of Unreachablemanage devices.
	    * @return unreachableDeviceList
	    * @throws Exception the exception
	    */
	   public static List<String> getUnreachableManagedDeviceIps()throws Exception
	   {
		   String query = "select `ipaddress` AS `ipaddress`, `hostname` AS `hostname` "
				   + "from discovered_devices where current_state = 'Unreachable'";

		   ResultSet rs = null;
		   List<String> unreachableDeviceList = new ArrayList<String>();
		   try 
		   {
			   rs = DBHelper.executeQuery(query);
			   while (rs.next())
			   {
				   unreachableDeviceList.add(rs.getString("ipaddress"));
			   }
		   } 
		   catch (Exception exp)
		   {
			   logger.error("Exception " + exp);
		   }
		   finally 
		   {
			   try 
			   {
				   rs.close();
			   } 
			   catch (Exception ee)
			   {
				   logger.debug("Error while closing result set", ee);
			   }
		   }
		   return unreachableDeviceList;
	   }

	   /**
	    * Get Unreachable devices.
	    *
	    * @return the list of values
	    * @throws Exception the exception
	    */ 
	   public static List<String> getUnreachableDeviceIps()throws Exception
	   {
		   String query = "select `ipaddress` AS `ipaddress`, `hostname` AS `hostname` "
				   + "from discovered_devices where current_state = 'Unreachable'";

		   ResultSet rs = null;
		   List<String> unreachableDeviceList = new ArrayList<String>();

		   try 
		   {
			   rs = DBHelper.executeQuery(query);
			   while (rs.next())
			   {
				   unreachableDeviceList.add(rs.getString("ipaddress"));
			   }
		   } 
		   catch (Exception exp)
		   {
			   logger.error("Exception " + exp);
		   }

		   finally 
		   {
			   try 
			   {
				   rs.close();
			   } 
			   catch (Exception ee)
			   {
				   logger.debug("Error while closing result set", ee);
			   }
		   }
		   return unreachableDeviceList;
	   }

	   /**
	    * Get Unreachable devices nodes.
	    *
	    * @return the list of values
	    * @throws Exception the exception
	    */ 
	   public static Integer[] getManageUnreachableDeviceNode() throws Exception
	   {

		   String query = "select `ipaddress` AS `ipaddress`"
				   + "from discovered_devices where current_state = 'Unreachable'";

		   ResultSet rs = null;
		   List<Integer> nodeIds = new ArrayList<Integer>();
		   try 
		   {
			   rs = DBHelper.executeQuery(query);
			   while (rs.next())
			   {
				   String  ipaddress= rs.getString("ipaddress");
				   NetworkNode networkNode=NetworkNodeCache.getInstance().getNode(ipaddress);
				   if(networkNode!=null) nodeIds.add(networkNode.getNodeId());
			   }		   

		   }
		   catch (Exception ex)
		   {
			   logger.warn();
		   } 
		   finally
		   {
			   try 
			   {
				   rs.close();
			   } 
			   catch (Exception ee)
			   {
				   logger.debug("Error while closing result set", ee);
			   }  
		   }
		   Integer nodeId[]=new Integer[nodeIds.size()];
		   return nodeIds.toArray(nodeId);

	   }

	   /**
	    * Removes the devices.
	    * @throws Exception the exception
	    */
	   public static void deleteDiscoveredDevices(List<String> deviceIp) throws Exception
	   {
		   try 
		   {
			   String ipaddress="";
			   StringBuilder devList=new StringBuilder();
			   Iterator<String> itr=deviceIp.iterator();
			   while(itr.hasNext())
			   {
				   devList.append("'").append(itr.next()).append("',");
			   }
			   if(devList.length()>0)
			   {
				   ipaddress=devList.substring(0,devList.length() - 1);
			   }
			   DBHelper.executeUpdate("delete from discovered_devices where ipaddress in ("+ipaddress+")");
		   }
		   catch (Exception ex)
		   {
			   logger.warn();
		   } 
	   }
	   
	   public static int getNonSnmpDevicesCount(ServiceDescriptor descriptor, Map<String, Object> parameterMap) throws Exception {
		   StringBuilder query = new StringBuilder();
		   query.append("select distinct ipaddress from discovered_devices where discovery_method = 'Nmap' and current_state = 'Reachable' "); //Fix for CSCuw10367: Not able move devices discovered using nmap to managed devices using CLI 
		   
		   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
			   String filterStr = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
	         	 if(!filterStr.isEmpty()){
	         		query.append(" AND "+filterStr);
	         	 }
	   		}
		   
		   return DBHelper.getRowCountFrmQuery(query.toString());
		   
	   }

	   public static int getUnreachableDevicesCount(ServiceDescriptor descriptor, Map<String, Object> parameterMap) throws Exception {
		   StringBuilder query = new StringBuilder();
		   query.append("select distinct ipaddress from discovered_devices where current_state = 'Unreachable'"); 
 
		  if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
			  String filterStr = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
	         	 if(!filterStr.isEmpty()){
	         		query.append(" AND "+filterStr);
	         	 }
		  }
		  
		   return DBHelper.getRowCountFrmQuery(query.toString());
		   
	   }
	   
	   public static int getDuplicateDevicesCount(ServiceDescriptor descriptor, Map<String, Object> parameterMap) throws Exception {
		   StringBuilder query = new StringBuilder();
		   query.append("select n.ipaddress as ipaddress, nep.prop_value as primary_device_name,duplicate_node_ipaddress as duplicate_ip,MANAGED_node_id as nodeid,TIME_LAST_ADDED as time_added from " +
	   		"node_ext_properties nep ,duplicate_nodes dn,nodes n where MANAGED_node_id=nep.id and nep.id = n.id  and prop_name='PRIMARY_DEVICE_NAME' and n.state = 'm'"); 
 
		   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
				String filterQuery = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
				if(!filterQuery.isEmpty()){
					query.append(" AND "+filterQuery);
				}
			}
		  
		   return DBHelper.getRowCountFrmQuery(query.toString());
		   
	   }
	   
	   public static Set<Integer> getManagedDevices(Map parameterMap, boolean isCountQeury) throws Exception {
		   StringBuilder query = new StringBuilder();
		   query.append("SELECT n.id" + //,ipaddress,hostname,sysoid,family,productid,serial_number,vendor,os,VERSION,ts_discovered,devicesource
		   		" FROM nodes n, nodes_addtl_details nad, ios_version iv" +
		   		" WHERE n.id = nad.id" +
		   		" AND n.id = iv.id and n.workflow_id='"+-1+"'"+" and isManaged='true'"); 
		   
		   if (parameterMap != null && parameterMap.get("selectedDevices") != null) {
				ArrayList selectedDevices = (ArrayList)parameterMap.get("selectedDevices");
				String inClauseForIds = StringUtils.join(selectedDevices, ",");
				if(!inClauseForIds.isEmpty()){
					query.append(" AND n.id IN ("+inClauseForIds+")");
				}
			}
		   
		   if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
				String filterQuery = ((PagedTreeFilterCfg)parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT)).getFilterQuery((String)parameterMap.get(IPariMain.SERVICENAME));
				if(!filterQuery.isEmpty()){
					query.append(" AND "+filterQuery);
				}
			}
		   
		   if (!isCountQeury && parameterMap != null && parameterMap.containsKey(IPariMain.SORTINGCONTEXT)) {
				query.append(((SortingContext)parameterMap.get(IPariMain.SORTINGCONTEXT)).getSortingQuery((String)parameterMap.get(IPariMain.SERVICENAME)));
			}
			
			if (!isCountQeury && parameterMap != null && parameterMap.containsKey(IPariMain.PAGINGCONTEXT)) {
				query.append(((PagingContext)parameterMap.get(IPariMain.PAGINGCONTEXT)).getPagingString());
			}
			
		   ResultSet rs = null;
		   Set<Integer> managedDevices = new LinkedHashSet<Integer> ();
		   
		      try
		      {
		         rs = DBHelper.executeQuery(query.toString());
		         while ( rs.next() ) {
		        	 managedDevices.add(rs.getInt(1));
		         }
		      } catch (Exception exp) {
		    	  logger.error("Exception " + exp);
		      } finally {
				  DBHelper.releaseResource(rs);
		      }
		   return managedDevices;   
	   }
	   
	  
	   public static String getDiscoveryMethod(String ipaddress){
		   String  discoveryMethod = null;
		   String query = "select discovery_method from discovered_devices where ipaddress='"+ipaddress+"'";
		   ResultSet rs = null;
		   try 
		   {
			   rs = DBHelper.executeQuery(query);
			   while(rs.next()){
				   discoveryMethod = rs.getString("discovery_method");
			   }
		   }
		   catch (Exception ex)
		   {
			   logger.error("Exception " + ex);
		   } 
		   finally
		   {
			   try 
			   {
				   rs.close();
			   } 
			   catch (Exception ee)
			   {
				   logger.debug("Error while closing result set", ee);
			   }  
		   }
		  return discoveryMethod;
	   }

	public static Map<ProtocolType, String[]> getProtocolsForDevices(String[] ipaddressArray) {

		Map<ProtocolType, String[]> protocolDeviceMap = new LinkedHashMap<>();
		Connection conn = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			List<String> ipaddressList = Arrays.<String>asList(ipaddressArray);
			StringBuffer query = new StringBuffer(
					"select discoveredBy as protocol,group_concat(ipaddress) as ipaddresses from discovered_devices ");
			LinkedHashMap<String, List<?>> inParamMap = new LinkedHashMap<String, List<?>>();
			if (!LittleUtils.isEmpty(ipaddressList)) {
				inParamMap.put(" where ipaddress ", ipaddressList);
			}
			StringBuffer filters = new StringBuffer(" and discoveredBy is not null GROUP BY discoveredBy");
			pstmt = DBHelper.createPrepareStatementWithFilter(conn, query, 1, inParamMap, filters);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String protocol = rs.getString("protocol");
				String ipaddresses = rs.getString("ipaddresses");
                protocolDeviceMap.put(ProtocolType.getEnumTypeFromString(protocol.toLowerCase()), ipaddresses.split(","));   
			}
		} catch (Exception ex) {
			logger.error("Error while loading getProtocolsForDevices: ", ex);
		} finally {
			DBHelper.releaseResource(rs);
			DBHelper.releaseResource(pstmt);
			DBHelper.releaseResource(conn);
		}
		return protocolDeviceMap;

	}

	public static List<String> getRachableDevices(String[] ipaddressArray) {

		List<String> reachableDevices = new ArrayList<String>();
		Connection conn = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			List<String> ipaddressList = Arrays.<String>asList(ipaddressArray);
			StringBuffer query = new StringBuffer(
					"select ipaddress from nodes where state='m' and status='re' and ");
			LinkedHashMap<String, List<?>> inParamMap = new LinkedHashMap<String, List<?>>();
			if (!LittleUtils.isEmpty(ipaddressList)) {
				inParamMap.put("  ipaddress ", ipaddressList);
			}
			pstmt = DBHelper.createPrepareStatement(conn, query, 1, inParamMap);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String ipaddress = rs.getString("ipaddress");
				reachableDevices.add(ipaddress);
			}
		} catch (Exception ex) {
			logger.error("Error while loading getRachableDevices: ", ex);
		} finally {
			DBHelper.releaseResource(rs);
			DBHelper.releaseResource(pstmt);
			DBHelper.releaseResource(conn);
		}
		return reachableDevices;

	}

   
   public static void main(String agrs[]) {
	   
	   try {
		DiscoveredDevice dd = getNonSnmpDevice("172.20.70.138");
		logger.debug("DD = " + dd.getDiscovery_method());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.error(e);
	}
	   
   }
}
