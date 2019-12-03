package com.pari.nm.utils.db;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cisco.web.mw.core.PagedTreeFilterCfg;
import com.cisco.web.mw.core.PagingContext;
import com.cisco.web.mw.core.SortingContext;
import com.pari.base.so.NetworkNode;
import com.pari.base.so.defs.IpAddressType;
import com.pari.base.so.defs.NodeIpAddress;
import com.pari.i18n.MsgCode;
import com.pari.logger.LoggerNames;
import com.pari.logger.PariLogger;
import com.pari.logger.PariLoggerFactory;
import com.pari.nm.groups.GroupingService;
import com.pari.nm.gui.guiservices.PariMsgException;
import com.pari.nm.modules.session.NetworkNodeCache;
import com.pari.nm.utils.IpUtils;
import com.pari.nm.utils.LittleUtils;
import com.pari.nm.utils.StringUtils;
import com.pari.nm.utils.InventoryProfiling;
import com.pari.server.IPariMain;

public class DiscoveryDBHelper2 {
	/**
	 * The Constant logger.
	 */
	private static final PariLogger logger = PariLoggerFactory.getLogger(LoggerNames.DISCOVERY);

	/**
	 * Update do not manage device list.
	 * 
	 * @param ipList
	 *            the ip list
	 */
	public static void addDoNotManageDeviceList(String[] ipList) {
		Connection c = null;
		try {
			c = DBHelper.getConnection();
			c.setAutoCommit(false);

			// DBHelper.executeUpdateNoCommit(c, "delete from do_not_manage");
			if (ipList != null) {
				for (int i = 0; i < ipList.length; i++) {
					DBHelper.executeUpdateNoCommit(c, "insert into do_not_manage VALUES('" + ipList[i] + "')");
				}
			}
			c.commit();
		} catch (Exception ee) {
			logger.warn("Error while trying to save Do not manage list.", ee);
			try {
				if (c != null) {
					c.rollback();
				}
			} catch (Exception el) {
				logger.debug("Error while rolling back the Do not manage list", el);
			}
		} finally {
			try {
				c.setAutoCommit(true);
			} catch (Exception e) {
				logger.debug("Error while Setting auto commit", e);
			}
			DBHelper.releaseConnection(c);
		}
		refreshDoNotManageIpListCache();
		cacheDoNotManageList();
	}

	/**
	 * The do not manage list.
	 */
	private static HashSet<IpAddressType> doNotManageList = new HashSet<IpAddressType>();
	/**
	 * The ip list.
	 */
	private static String[] ipList = null;

	/**
	 * The ip list.
	 */
	private static ArrayList<String> ipAddressList = null;

	public static HashSet<IpAddressType> getCachedDoNotManageList() {
		return doNotManageList;
	}

	public static String[] getCachedDoNotManageIpList() {
		if (ipList == null)
			ipList = getDoNotManageList();

		return ipList;
	}

	public static void refreshDoNotManageIpListCache() {
		ipList = getDoNotManageList();
		cacheDoNotManageList();
	}

	public static String[] getPagedDoNotManageIpList(HashMap parameters) throws Exception {
		return getDoNotManageList(parameters);
	}

	public static String[] getCachedDoNotManageIpAddressList() {
		if ((ipAddressList == null) || (ipAddressList.isEmpty())) {
			cacheDoNotManageList();
		}
		return ipAddressList.toArray(new String[ipAddressList.size()]);
	}

	/**
	 * Sets the do not manage list.
	 *
	 * @param ipList
	 *            the new do not manage list
	 */
	public static void cacheDoNotManageList() {
		DiscoveryDBHelper2.ipList = getCachedDoNotManageIpList();
		doNotManageList = new HashSet<IpAddressType>();
		ipAddressList = new ArrayList<String>();
		for (int i = 0; i < ipList.length; i++) {
			try {
				IpAddressType iT = StringUtils.getIpAddressType(ipList[i]);
				if (iT != null) {
					doNotManageList.add(iT);
					// add resolved hostname, ip address or ip range.
					if (iT.getType() == IpAddressType.IPADDRESS) {
						ipAddressList.add(((NodeIpAddress) iT).getInetAddress().getHostAddress());
					} else {
						ipAddressList.add(ipList[i]);
					}
				} else {
					logger.error("Do Not Manage List - Device name cannot be resolved" + ipList[i]);
				}
			} catch (Exception ee) {
				logger.error("Error in creating DonotManageList.", ee);
			}
		}
	}

	/**
	 * Gets the do not manage list.
	 * 
	 * @return the do not manage list
	 */
	public static String[] getDoNotManageList() {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = DBHelper.executeQuery("select * from do_not_manage");
			while (rs.next()) {
				String ip = rs.getString("iplist").trim();
				if (!IpUtils.validateIpAddress(ip)) {
					IpAddressType iT = StringUtils.getIpAddressType(ip);
					if (iT != null) {
						list.add(iT.toString());
					} else {
						logger.error("Do Not Manage List - Device name cannot be resolved" + ip);
					}
					continue;
				}
				list.add(ip);
			}
		} catch (Exception ee) {

		} finally {
			try {
				rs.close();
			} catch (Exception ee) {
			}
		}
		String[] lis = new String[list.size()];
		list.toArray(lis);

		return lis;
	}

	public static String[] getDoNotManageList(HashMap parameterMap) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		StringBuffer query = new StringBuffer();
		query.append("select * from do_not_manage");

		if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGEDTREEFILTERCONTEXT)) {
			String filterQuery = ((PagedTreeFilterCfg) parameterMap.get(IPariMain.PAGEDTREEFILTERCONTEXT))
					.getFilterQuery((String) parameterMap.get(IPariMain.SERVICENAME));
			if (!filterQuery.isEmpty()) {
				query.append(" where " + filterQuery);
			}
		}

		if (parameterMap != null && parameterMap.containsKey(IPariMain.SORTINGCONTEXT)) {
			query.append(((SortingContext) parameterMap.get(IPariMain.SORTINGCONTEXT))
					.getSortingQuery((String) parameterMap.get(IPariMain.SERVICENAME)));
		}

		if (parameterMap != null && parameterMap.containsKey(IPariMain.PAGINGCONTEXT)) {
			query.append(((PagingContext) parameterMap.get(IPariMain.PAGINGCONTEXT)).getPagingString());
		}

		try {
			rs = DBHelper.executeQuery(query.toString());
			while (rs.next()) {
				String ip = rs.getString("iplist").trim();
				if (!IpUtils.validateIpAddress(ip)) {
					IpAddressType iT = StringUtils.getIpAddressType(ip);
					if (iT != null) {
						list.add(iT.toString());
					} else {
						logger.error("Do Not Manage List - Device name cannot be resolved" + ip);
					}
					continue;
				}
				list.add(ip);
			}
		} catch (Exception ee) {

		} finally {
			try {
				rs.close();
			} catch (Exception ee) {
			}
		}
		String[] lis = new String[list.size()];
		list.toArray(lis);

		return lis;
	}

	public static void deleteAllDoNotManageDevices() throws Exception {
		ResultSet rs = null;

		StringBuffer query = new StringBuffer();
		query.append("delete from do_not_manage ");
		try {
			rs = DBHelper.executeQuery(query.toString());

		} catch (Exception ee) {

		} finally {
			try {
				rs.close();
			} catch (Exception ee) {
			}
		}
		refreshDoNotManageIpListCache();
	}

	public static void deleteDoNotManageDevices(String[] devices) {
		ResultSet rs = null;

		StringBuffer query = new StringBuffer();
		String device_list = String.join("','", devices);
		device_list = "'" + device_list + "'";

		query.append("DELETE FROM do_not_manage where iplist IN (" + device_list + ")");
		try {
			rs = DBHelper.executeQuery(query.toString());

		} catch (Exception ee) {

		} finally {
			try {
				rs.close();
			} catch (Exception ee) {
			}
		}
		refreshDoNotManageIpListCache();
	}

	public static void updateAdditionalDeviceProperties(List<InventoryProfiling> list, boolean insert) throws Exception {
		Connection c = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			c.setAutoCommit(false);
			ArrayList<String> nonManagedDevices = new ArrayList<String>();
			for (InventoryProfiling device : list) {
				String selectSQL = "select id from nodes where ipaddress= ? ";
				Object[] values = new Object[] { device.getIpAddress() };
				rs = DBHelper.executeQuery(selectSQL, values);
				int deviceId = 0;
				while (rs.next()) {
					deviceId = rs.getInt("id");
				}
				if(deviceId != 0) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("FAMILY", device.getFamily());
					map.put("OSTYPE", device.getOsType());
					map.put("TECHNOLOGY", device.getTechnologies());
					
					int insertCount = 0, updateCount = 0;
					ps1 = c.prepareStatement(
							"insert into node_ext_properties (id, prop_name, prop_value) VALUES (?, ?, ?) ");
					ps2 = c.prepareStatement(
							"delete from node_ext_properties where id= ? and prop_name= ? ");
					
					for (String propName : map.keySet()) {
						String propValue = map.get(propName).trim();
						if ((propValue != null) && (propValue.length() > 0)) {
							if(!insert){
								ps2.setInt(1, deviceId);
								ps2.setString(2, propName);
								updateCount++;
								ps2.addBatch();
							}
							
							ps1.setInt(1, deviceId);
							ps1.setString(2, propName);
							ps1.setString(3, propValue);
							insertCount++;
							ps1.addBatch();
						}
					}
					if (updateCount > 0)
						ps2.executeBatch();
					if (insertCount > 0)
						ps1.executeBatch();
					c.commit();
					
					NetworkNode node = NetworkNodeCache.getInstance().getNodeByID((Integer) deviceId);
					node.setInventoryProfilingFamily(device.getFamily());
					node.setInventoryProfilingTechnology(device.getTechnologies());
					node.setInventoryProfilingOSType(device.getOsType());
					
					NetworkNodeCache.getInstance().updateNode(node, device.getIpAddress());
					GroupingService.getInstance().updateGroups(node, true);
				} else {
					nonManagedDevices.add(device.getIpAddress());
				}
			}
			if(! LittleUtils.isEmpty(nonManagedDevices)) {
				throw new PariMsgException("TBD", "Additional properties couldn't be added/modified for following non Managed Devices: " + nonManagedDevices);
			}
		} catch (NullPointerException e) {
			logger.error("Parameter is not set.", e);
			throw new PariMsgException(MsgCode.TO_CODE24, e);
		} catch (BatchUpdateException e) {
			logger.error("Duplicate entry of properties for the device", e);
			throw new PariMsgException(MsgCode.TO_CODE23, e);
		} catch (Exception ee) {
			try {
				if (c != null)
					c.rollback();
			} catch (Exception sqlEx) {
				logger.debug("Error while rolling back the data", sqlEx);
			}
			logger.warn("Error while updating device properties", ee);
			throw new PariMsgException("TBD", ee.getLocalizedMessage());
		} finally {
			try {
				c.setAutoCommit(true);
			} catch (Exception e) {
				logger.debug("Error while Setting auto commit", e);
			}
			
			DBHelper.releaseResource(rs, ps1, ps2, c);
		}
	}

	public static void deleteAdditionalDeviceProperties(List<String> deviceList) throws Exception {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = DBHelper.getConnection();
			c.setAutoCommit(false);
			ArrayList<String> nonManagedDevices = new ArrayList<String>();
			for (String ipAddress : deviceList) {
				String selectSQL = "select id from nodes where ipaddress= ? ";
				Object[] values = new Object[] { ipAddress };
				rs = DBHelper.executeQuery(selectSQL, values);
				int deviceId = 0;
				while (rs.next()) {
					deviceId = rs.getInt("id");
				}
				if (deviceId != 0) {
					ps = c.prepareStatement(
							"delete from node_ext_properties where id = " +  deviceId + " and prop_name in ('FAMILY', 'OSTYPE','TECHNOLOGY') ");
					ps.executeUpdate();
					c.commit();
					
					NetworkNode node = NetworkNodeCache.getInstance().getNodeByID((Integer) deviceId);
					node.setInventoryProfilingFamily("");
					node.setInventoryProfilingTechnology("");
					node.setInventoryProfilingOSType("");
					
					NetworkNodeCache.getInstance().updateNode(node, ipAddress);
					GroupingService.getInstance().updateGroups(node, true);
				} 
				else {
					nonManagedDevices.add(ipAddress);
				}
			}
			if(! LittleUtils.isEmpty(nonManagedDevices)) {
				throw new PariMsgException("TBD", "Additional properties couldn't be added/modified for following non Managed Devices: " + nonManagedDevices);
			}
		} catch (Exception ee) {
			try {
				if (c != null)
					c.rollback();
			} catch (Exception sqlEx) {
				logger.debug("Error while rollingback the data", sqlEx);
			}
			logger.warn("Error while deleting device properties", ee);
			throw new PariMsgException("TBD", ee.getLocalizedMessage());
		} finally {
			try {
				c.setAutoCommit(true);
			} catch (Exception e) {
				logger.debug("Error while Setting auto commit", e);
			}
			DBHelper.releaseResource(rs, ps, c);
		}
	}
	
	public static Map<String, InventoryProfiling> getAdditionalDeviceProperties(List<String> deviceList) throws Exception {
		ResultSet rs = null;
		Map<String, InventoryProfiling> map = new HashMap<String, InventoryProfiling>();
		try {
			ArrayList<String> nonManagedDevices = new ArrayList<String>();
			for (String ipAddress : deviceList) {
				String selectSQL = "select id from nodes where ipaddress= ? ";
				Object[] values = new Object[] { ipAddress };
				rs = DBHelper.executeQuery(selectSQL, values);
				int deviceId = 0;
				while (rs.next()) {
					deviceId = rs.getInt("id");
				}
				if (deviceId != 0) {
					NetworkNode node = NetworkNodeCache.getInstance().getNodeByID((Integer) deviceId);
					InventoryProfiling device = new InventoryProfiling();
					device.setIpAddress(ipAddress);
					device.setFamily(LittleUtils.isEmpty(node.getInventoryProfilingFamily()) ? "" : node.getInventoryProfilingFamily());
					device.setOsType(LittleUtils.isEmpty(node.getInventoryProfilingOSType()) ? "" : node.getInventoryProfilingOSType());
					device.setTechnologies(LittleUtils.isEmpty(node.getInventoryProfilingTechnology()) ? "" : node.getInventoryProfilingTechnology());
					map.put(ipAddress, device);
				} else {
					nonManagedDevices.add(ipAddress);
				}
			} 
			if(! LittleUtils.isEmpty(nonManagedDevices)) {
				throw new PariMsgException("TBD", "Additional properties couldn't be added/modified for following non Managed Devices: " + nonManagedDevices);
			}
		} catch (Exception ee) {
			logger.warn("Error while retrieving device properties.", ee);
			throw new PariMsgException("TBD", ee.getLocalizedMessage());
		} finally {
			DBHelper.releaseResource(rs);
		}
		return map;
	}
}
