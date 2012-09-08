package org.iemm.sicomoro.service;

import java.util.List;

import org.iemm.sicomoro.db.client.CatalogMapper;
import org.iemm.sicomoro.db.dao.Catalog;
import org.iemm.sicomoro.db.dao.CatalogExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

	@Autowired
	private CatalogMapper catalogMapper;

	/**
	 * Regresa un string con el valor de un catalogo en base a la unique key
	 * group - name. Lanza IndexOutOfBoundException si no existe el catalogo en la
	 * base de datos
	 * 
	 * @param group
	 * @param name
	 * @return
	 */
	public String getCatalog(String group, String name) {
		final CatalogExample example = new CatalogExample();
		example.createCriteria().andGroupEqualTo(group).andNameEqualTo(name);
		final List<Catalog> lstCatalog = catalogMapper.selectByExample(example);
		return lstCatalog.get(0).getValue();
	}

	/**
	 * @return the catalogMapper
	 */
	public CatalogMapper getCatalogMapper() {
		return catalogMapper;
	}

	/**
	 * @param catalogMapper
	 *            the catalogMapper to set
	 */
	public void setCatalogMapper(CatalogMapper catalogMapper) {
		this.catalogMapper = catalogMapper;
	}

}
