package ua.kpi.getman.InternetShop.web.command;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.kpi.getman.InternetShop.Path;
import ua.kpi.getman.InternetShop.db.DBManager;
import ua.kpi.getman.InternetShop.dto.CategoryDTO;
import ua.kpi.getman.InternetShop.exception.AppException;
import ua.kpi.getman.InternetShop.exception.DBException;

public class CategoryCommand extends Command {
	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(RegistrationCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		languages(request);

		String forward = Path.PAGE_MAIN;
		String strAction = request.getParameter("action");
		try {
			switch (strAction) {
			case "category":
				brands(request);
				allCategories(request, localization(request));
				products(request, localization(request));
				forward = Path.PAGE_MAIN;
				break;
			case "categoryDelete":
				categoryDelete(request);
				forward = Path.PAGE_ADMIN_PANEL;
				break;
			case "categoryCreate":

				categoryCreate(request);

				forward = Path.PAGE_ADMIN_PANEL;
				break;
			case "categoryUpdate":
				
				categoryUpdate(request);
				forward = Path.PAGE_ADMIN_PANEL;
				break;
			case "categoryDirection":
				String type = request.getParameter("type");
				request.setAttribute("type", type);
				if ("update".equals(type)) {
					categoryValuesForUpdate(request);
				}

				forward = Path.PAGE_ADMIN_CATEGORY_CREATE;

				break;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		response.setContentType("text/html;charset=utf-8");

		LOG.debug("Command finished");
		return forward;
	}

	public static void products(HttpServletRequest request, long langId) throws AppException {
		String categoryId = request.getParameter("categoryId");
		DBManager manager = DBManager.getInstance();

		if ((categoryId != null) && !(categoryId.isEmpty())) {
			LOG.trace("Request parameter: categoryId --> " + categoryId);

			request.setAttribute("products", manager.findProductsByCategoryId(Long.valueOf(categoryId), langId));

		} else {
			if (!sort(request, langId)) {
				request.setAttribute("products", manager.findProducts(langId));
			}
		}

	}

	protected void brands(HttpServletRequest request) throws DBException {
		DBManager manager = DBManager.getInstance();
		request.setAttribute("brandList", manager.findBrands());
	}

	protected void categoryDelete(HttpServletRequest request) throws DBException {
		String categoryId = request.getParameter("categoryId");
		DBManager manager = DBManager.getInstance();
		if (categoryId != null && !categoryId.isEmpty()) {
			manager.deleteCategoryById(Long.valueOf(categoryId));
		}
	}

	protected void categoryCreate(HttpServletRequest request) throws DBException, SQLException {
		DBManager manager = DBManager.getInstance();
		
		
		CategoryDTO categoryForCreate = extractToCategoryDTO(request);
		manager.createCategory(categoryForCreate);


	}
	private CategoryDTO extractToCategoryDTO(HttpServletRequest request) throws DBException, SQLException {
		CategoryDTO categoryForCreate = new CategoryDTO();

		String ruCategoryName = request.getParameter("ruCategoryName");

		String enCategoryName = request.getParameter("enCategoryName");

		String sortOrder = request.getParameter("sortOrder");

		categoryForCreate.setEnCategoryTitle(enCategoryName);
		categoryForCreate.setRuCategoryTitle(ruCategoryName);
		categoryForCreate.setSortOrder(Integer.valueOf(sortOrder));

		LOG.debug("ruCategoryName ==> " + ruCategoryName);
		LOG.debug("enCategoryName ==> " + enCategoryName);
		LOG.debug("sortOrder ==> " + sortOrder);
		return categoryForCreate;

	}

	protected void categoryValuesForUpdate(HttpServletRequest request) throws DBException {
		String categoryId = request.getParameter("categoryId");
		LOG.debug("categoryId ==> " + categoryId);
		DBManager manager = DBManager.getInstance();
		
		request.setAttribute("categoryDTO",manager.findCategoryForUpdateById(Long.valueOf(categoryId)));
		request.setAttribute("categoryId", categoryId);
		

	}
	protected void categoryUpdate(HttpServletRequest request) throws DBException, SQLException {
		
		CategoryDTO categoryForCreate = extractToCategoryDTO(request);
		DBManager manager = DBManager.getInstance();
		
		String categoryId = request.getParameter("categoryId");
		
	
		
		manager.updateCategoryById(Long.valueOf(categoryId), categoryForCreate);
		
	}

	public static void allCategories(HttpServletRequest request, long langId) throws AppException {
		DBManager manager = DBManager.getInstance();
		request.setAttribute("categoriesList", manager.findCategories(langId));

	}

	protected static boolean sort(HttpServletRequest request, long langId) throws AppException {
		DBManager manager = DBManager.getInstance();

		String choosedBrand = request.getParameter("choosedBrand");
		String sortType = request.getParameter("sortType");
		String priceFrom = request.getParameter("priceFrom");
		String priceTo = request.getParameter("priceTo");
		LOG.trace(choosedBrand);
		LOG.trace(sortType);
		LOG.trace(priceFrom);
		LOG.trace(priceTo);

		// All parametrs choosed
		if ((choosedBrand != null) && (sortType != null) && (priceFrom != null) && (priceTo != null)
				&& !(choosedBrand.isEmpty()) && !(sortType.isEmpty()) && !(priceFrom.isEmpty())
				&& !(priceTo.isEmpty())) {
			LOG.trace("all sortTypes != null");

			if ((Float.valueOf(priceFrom) > 0) && (Float.valueOf(priceTo) > 0)
					&& (Float.valueOf(priceFrom) < Float.valueOf(priceTo))) {
				request.setAttribute("products", manager.sortProducts(choosedBrand, sortType, Float.valueOf(priceFrom),
						Float.valueOf(priceTo), langId));
				return true;
			}

			// Brand is not choosed
		} else if (((choosedBrand == null) || (choosedBrand.isEmpty())) && (sortType != null) && !(sortType.isEmpty())
				&& (priceFrom != null) && (priceTo != null) && !(priceFrom.isEmpty()) && !(priceTo.isEmpty())) {

			if ((Float.valueOf(priceFrom) > 0) && (Float.valueOf(priceTo) > 0)
					&& (Float.valueOf(priceFrom) < Float.valueOf(priceTo))) {
				request.setAttribute("products",
						manager.sortProducts(sortType, Float.valueOf(priceFrom), Float.valueOf(priceTo), langId));
				return true;
			}

			// Brand,TypeSort are not choosed
		} else if (((choosedBrand == null) || (choosedBrand.isEmpty())) && ((sortType == null) || (sortType.isEmpty()))
				&& (priceFrom != null) && (priceTo != null) && !(priceFrom.isEmpty()) && !(priceTo.isEmpty())) {
			if ((Float.valueOf(priceFrom) > 0) && (Float.valueOf(priceTo) > 0)
					&& (Float.valueOf(priceFrom) < Float.valueOf(priceTo))) {
				request.setAttribute("products",
						manager.sortProducts(Float.valueOf(priceFrom), Float.valueOf(priceTo), langId));
				return true;
			}
			// // Brand,TypeSort,fromPrice are not choosed
		} else if (((choosedBrand == null) || (choosedBrand.isEmpty())) && ((sortType == null) || (sortType.isEmpty()))
				&& ((priceFrom == null) || (priceFrom.isEmpty())) && (priceTo != null) && !(priceTo.isEmpty())) {
			request.setAttribute("products", manager.sortProducts(Float.MIN_VALUE, Float.valueOf(priceTo), langId));

			return true;

			// // Brand,TypeSort,toPrice are not choosed
		} else if (((choosedBrand == null) || (choosedBrand.isEmpty())) && ((sortType == null) || (sortType.isEmpty()))
				&& ((priceFrom != null) && !(priceFrom.isEmpty())) && ((priceTo == null) || (priceTo.isEmpty()))) {
			request.setAttribute("products", manager.sortProducts(Float.valueOf(priceFrom), Float.MAX_VALUE, langId));

			return true;

			// // // Brand,TypeSort,fromPrice,toPrice are not choosed
		} else if (((choosedBrand == null) || (choosedBrand.isEmpty())) && ((sortType == null) || (sortType.isEmpty()))
				&& ((priceFrom == null) || (priceFrom.isEmpty())) && ((priceTo == null) || (priceTo.isEmpty()))) {

			return false;

			// Brand,price from,to not choosed
		} else if ((((choosedBrand == null) || (choosedBrand.isEmpty())) && (sortType != null) && !(sortType.isEmpty())
				&& ((priceFrom == null) || (priceFrom.isEmpty())) && (((priceTo == null) || (priceTo.isEmpty()))))) {
			request.setAttribute("products", manager.sortProducts(choosedBrand, sortType, langId));
			return true;
		} // price from,to not choosed
		else if ((((choosedBrand != null) && !(choosedBrand.isEmpty())) && (sortType != null) && !(sortType.isEmpty())
				&& ((priceFrom == null) || (priceFrom.isEmpty())) && (((priceTo == null) || (priceTo.isEmpty()))))) {
			request.setAttribute("products", manager.sortProducts(choosedBrand, sortType, langId));

			return true;
		} // price,sortType from,to not choosed
		else if ((((choosedBrand != null) && !(choosedBrand.isEmpty())) && ((sortType == null) || (sortType.isEmpty()))
				&& ((priceFrom == null) || (priceFrom.isEmpty())) && ((priceTo == null) || (priceTo.isEmpty())))) {
			request.setAttribute("products", manager.sortProducts(choosedBrand, sortType, langId));

			return true;
		} else {
			LOG.trace("all sortTypes = null");
			return false;
		}
		return false;

	}

	public static void languages(HttpServletRequest request) throws DBException {
		DBManager manager = DBManager.getInstance();
		request.setAttribute("langList", manager.findLanguages());

	}

	public static long localization(HttpServletRequest request) {

		String languageParam = request.getParameter("language");

		HttpSession session = request.getSession();
		String language = (String) session.getAttribute("language");

		if (languageParam != null) {
			request.getSession().setAttribute("language", languageParam);
		}

		LOG.trace("lang param 1 is " + languageParam);

		LOG.trace("lang session  " + language);
		return Long.valueOf(language);

	}
}
