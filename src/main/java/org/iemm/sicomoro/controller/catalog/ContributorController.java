package org.iemm.sicomoro.controller.catalog;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/catalog/contributor/")
public class ContributorController {

	@Autowired
	private ContributorService contributorService;
	
	private Logger LOG = LoggerFactory.getLogger(ContributorController.class);

	
	@RequestMapping("/all")
	public ModelAndView all(HttpServletRequest request, ModelAndView model) {
		LOG.trace("ContributorController.all");
		org.apache.log4j.Logger.getLogger(ContributorController.class).debug("Log4j Configurado");
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
		htmlRow.addColumn(secondLastName);

		final HtmlColumn edit = new HtmlColumn("idContributor");
		edit.setTitle("Editar");
		edit.setCellEditor(new ImageCellEditor("/sicomoro/resources/accessories-text-editor.png", "goToEdit"));
		htmlRow.addColumn(edit);

		tableModel.setTable(htmlTable);
 
		model.addObject("table", tableModel.render());
		model.setViewName("/jsp/catalog/contributor/all.jsp");
		return model;
	}

	@RequestMapping("/new")
	public String newObject(Model model) {
		model.addAttribute("contributor", new Contributor());
		return "/jsp/catalog/contributor/newAndEdit.jsp";
	}

	@RequestMapping("/{idContributor}/edit")
	public String editObject(@PathVariable Integer idContributor, Model model) {
		model.addAttribute("contributor",
				contributorService.find(idContributor));
		return "/jsp/catalog/contributor/newAndEdit.jsp";
	}
	@RequestMapping("/save")
	public String saveNewObject(@ModelAttribute Contributor contributor,
			Model model) {
		contributorService.save(contributor);
		return "redirect:/catalog/contributor/" + contributor.getIdContributor()
				+ "/edit.html";
	}
	// TODO ver la forma de mapear dos url a la misma funcion
	@RequestMapping("/**/save.html")
	public String saveObject(@ModelAttribute Contributor contributor,
			Model model) {
		contributorService.save(contributor);
		return "redirect:/catalog/contributor/" + contributor.getIdContributor()
				+ "/edit.html";
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
}
