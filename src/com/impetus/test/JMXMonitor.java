package com.impetus.test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXMonitor {

	public static void main(String[] args) throws InstanceNotFoundException, IntrospectionException,
			MalformedObjectNameException, ReflectionException {
		RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
		try {
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:10101/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection connection = jmxc.getMBeanServerConnection();
			String domains[] = connection.getDomains();
			for (String domain : domains) {
				System.out.println("\tDomain = " + domain);
			}
			printBeanInfo(connection.getMBeanInfo(getObjectName("kafka.server:type=KafkaServer,name=BrokerState")));
			printBeanInfo(connection.getMBeanInfo(
					getObjectName("kafka.cluster:type=Partition,name=UnderReplicated,topic=test,partition=0")));
			printBeanInfo(
					connection.getMBeanInfo(getObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec")));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void printBeanInfo(MBeanInfo mBeanInfo) {
		System.out.println(mBeanInfo.getClassName());
		System.out.println(mBeanInfo.toString());
		for (MBeanAttributeInfo attribute : mBeanInfo.getAttributes()) {
			System.out.println(attribute.toString());
		}
		System.out.println("==============");
	}

	public static ObjectName getObjectName(String type) throws MalformedObjectNameException {
		return new ObjectName(type);
	}

}
