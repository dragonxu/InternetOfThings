package com.tyloo.web.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.tyloo.fw.ServiceLocator;
import com.tyloo.modules.service.ShopUserService;

public class SqlFilter implements Filter {
	// private static ShopUserService shopUserService = (ShopUserService)
	// ServiceLocator.getService("shopUserService");
	private static String injStr = "";
	private static String excepStr = "";
	protected FilterConfig filterConfig = null;

	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
		injStr = filterConfig.getInitParameter("keywords");
		excepStr = filterConfig.getInitParameter("excep");
	}

	@SuppressWarnings("rawtypes")
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// �ж�login Pinkey�Ƿ����
		// CookieUtil cookie = new CookieUtil(req, res);
		// String fidStr = cookie.getClientValue("familyId");
		// if(StringUtils.isNotEmpty(fidStr))
		// {
		// try {
		// int fid = Integer.parseInt(fidStr);
		// Family f = familyService.getFamilyById(fid);
		// //����Ǯ����ͨ
		// if(f.getEpay()==1)
		// {
		// Object pin = req.getSession().getAttribute("pinKey");
		// if(pin==null)
		// {
		// res.sendRedirect(req.getContextPath() +
		// "/eportal/login.html?logout=y");
		//
		// return ;
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }

		// Iterator values = req.getParameterMap().values().iterator();//
		// ��̨����֤
		String url = req.getServletPath();
		if (url.indexOf("eportal") < 0) {
			chain.doFilter(req, res);
			return;
		}
		// ��ȡ���еı�����ֵ
		String sql = "";
		Enumeration paramsNames = req.getParameterNames();// ��ȡ���еı��������
		while (paramsNames.hasMoreElements()) {
			String name = paramsNames.nextElement().toString();
			// ��У����ֶ�
			if (excepStr.indexOf("," + name + ",") < 0) {
				String value[] = req.getParameterValues(name);

				for (int i = 0; i < value.length; i++) {
					// ���볤�ȴ���20λ����ת��error.html
					if (value[i].length() > 20) {
						System.out.println("�ֶ�:" + name + ",����:"
								+ value[i].length());
						System.out
								.println("============================BADLENGTH IP:"
										+ request.getRemoteHost());
						res.sendRedirect(req.getContextPath()
								+ "/eportal/sqlerrors.html");
						return;
					}
					sql = value[i];
					// ��sql�ؼ��֣���ת��error.html
					if (!StringUtils.isEmpty(sql) && sqlValidate(sql)) {
						System.out.println("�ֶ�:" + name);
						System.out
								.println("============================BADSQL:"
										+ sql);
						System.out
								.println("============================BADSQL IP:"
										+ request.getRemoteHost());
						res.sendRedirect(req.getContextPath()
								+ "/eportal/sqlerrors.html");
						return;
					}
				}
			}
		}
		chain.doFilter(req, res);
	}

	// Ч��
	protected static boolean sqlValidate(String str) {
		str = str.toLowerCase();// ͳһתΪСд
		String badStr = injStr;// ���˵���sql�ؼ��֣������ֶ����
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i]) != -1) {
				return true;
			} else {
				/*if (badStrs[i].equals("'")) {
					str = str.replaceAll("'", "''");
				} else if (badStrs[i].equals("\"")) {
					str = str.replaceAll("\"", "\"\"");
				}*/
			}
		}
		return false;
	}

	public static void main(String[] s) {
		System.out.println("aass".indexOf("a"));
	}

	public void destroy() {
		injStr = null;
		this.filterConfig = null;

	}

}
