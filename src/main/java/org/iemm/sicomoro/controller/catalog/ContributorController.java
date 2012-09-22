package org.iemm.sicomoro.controller.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iemm.sicomoro.controller.ComboView;
import org.iemm.sicomoro.controller.jmesa.ImageCellEditor;
import org.iemm.sicomoro.db.dao.Contributor;
import org.iemm.sicomoro.service.ContributorService;
import org.jmesa.model.TableModel;
import org.jmesa.view.html.component.HtmlColumn;
import org.jmesa.view.html.component.HtmlRow;
import org.jmesa.view.html.component.HtmlTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/catalog/contributor/")
public class ContributorController {

	@Autowired
	private ContributorService contributorService;
	
	@Autowired
	private MessageSource resource;
	
	private Logger LOG = LoggerFactory.getLogger(ContributorController.class);

	
	@RequestMapping("/all")
	public ModelAndView all(HttpServletRequest request, ModelAndView model) {
		LOG.trace("ContributorController.all");
		final TableModel tableModel = new TableModel("contributorTable", request);
		tableModel.setItems(contributorService.getAll());

		final HtmlTable htmlTable = new HtmlTable();

		final HtmlRow htmlRow = new HtmlRow();
		htmlTable.setRow(htmlRow);

		final HtmlColumn firstName = new HtmlColumn("name");
		htmlRow.addColumn(firstName);

		final HtmlColumn lastName = new HtmlColumn("lastName");
		htmlRow.addColumn(lastName);
		
		final HtmlColumn secondLastName = new HtmlColumn("secondLastName");
		secondLastName.setTitle(resource.getMessage("contributor.form.secondLastName", null, LocaleContextHolder.getLocale()));
		htmlRow.addColumn(secondLastName);

		final HtmlColumn edit = new HtmlColumn("idContributor");
		edit.setTitle(resource.getMessage("form.edit", null, LocaleContextHolder.getLocale()));
		edit.setCellEditor(new ImageCellEditor("/sicomoro/resources/accessories-text-editor.png", "goToEdit"));
		htmlRow.addColumn(edit);
		
		final HtmlColumn delete = new HtmlColumn("idContributor");
		delete.setTitle("Delete?");
		delete.setCellEditor(new ImageCellEditor("/sicomoro/resources/edit-delete.png", "delId"));
		htmlRow.addColumn(delete);

		tableModel.setTable(htmlTable);
 
		model.addObject("table", tableModel.render());
		model.setViewName("/jsp/catalog/contributor/all.jsp");
		return model;
	}
	
	@RequestMapping("/getList")
	public @ResponseBody List<ComboView> getList() {
		LOG.trace("getList");
		final List<ComboView> result = new ArrayList<ComboView>();
		final List<Contributor> all = contributorService.getAll();

		for (Contributor contributor : all) {
			final ComboView view = new ComboView();
			view.setValue(contributor.getIdContributor().toString());
			view.setText(String.format("%s %s %s", contributor.getName(),
					contributor.getLastName(), contributor.getSecondLastName()));
			result.add(view);
		}
		return result;
	}

	@RequestMapping("/new")
	public String newObject(Model model) {
		LOG.trace("ContributorController.new");
		model.addAttribute("contributor", new Contributor());
		return "/jsp/catalog/contributor/newAndEdit.jsp";
	}

	@RequestMapping("/{idContributor}/edit")
	public String editObject(@PathVariable Integer idContributor, Model model) {
		LOG.trace("ContributorController.edit");
		model.addAttribute("contributor",
				contributorService.find(idContributor));
		return "/jsp/catalog/contributor/newAndEdit.jsp";
	}
	
	@RequestMapping("/{idContributor}/delete")
	public String deleteObject(@PathVariable Integer idContributor, Model model) {
		LOG.trace("ContributorController.delete");
		model.addAttribute("contributor",
				contributorService.delete(idContributor));
		return "redirect:/catalog/contributor/all.html";
	}

	@RequestMapping(value = { "/save", "/**/save" })
	public String saveObject(@ModelAttribute Contributor contributor,
			Model model) {
		LOG.trace("ContributorController.save");
		contributorService.save(contributor);
		return "redirect:/catalog/contributor/all.html";
	}

	/**
	 * @return the contributorService
	 */
	public ContributorService getContributorService() {
		return contributorService;
	}

	/**
	 * @param contributorService
	 *            the contributorService to set
	 */
	public void setContributorService(ContributorService contributorService) {
		this.contributorService = contributorService;
	}

	/**
	 * @return the resource
	 */
	public MessageSource getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(MessageSource resource) {
		this.resource = resource;
	}

}
