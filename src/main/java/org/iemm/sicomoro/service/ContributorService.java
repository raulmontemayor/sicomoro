package org.iemm.sicomoro.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.iemm.sicomoro.db.client.ContributorMapper;
import org.iemm.sicomoro.db.dao.Contributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributorService {

	@Autowired
	private ContributorMapper contributorMapper;

	/**
	 * Inserta o actualiza un registro en la tabla de contribuidores
	 * 
	 * @param contributor
	 *            El objeto a insertar, no puede ser null
	 */
	public void save(Contributor contributor) {
		Validate.notNull(contributor);
		final Date actual = new Date();
		if (contributor.getIdContributor() == null) {
			contributor.setCreateDate(actual);
			contributor.setUpdateDate(actual);
			contributorMapper.insert(contributor);
		} else {
			contributor.setUpdateDate(actual);
			contributorMapper.updateByPrimaryKeySelective(contributor);
		}
	}

	/**
	 * Regresa el contribuidor por id
	 * 
	 * @param idContributor
	 *            El id, no puede ser null
	 * @return
	 */
	public Contributor find(Integer idContributor) {
		Validate.notNull(idContributor);
		return contributorMapper.selectByPrimaryKey(idContributor);
	}
	
	public int delete(Integer idContributor) {
		Validate.notNull(idContributor);
		return contributorMapper.deleteByPrimaryKey(idContributor);
	}


	/**
	 * Obtiene la lista de todos los contribuidores
	 * 
	 * @return
	 */
	public List<Contributor> getAll() {
		return contributorMapper.selectByExample(null);
	}

	/**
	 * @return the contributorMapper
	 */
	public ContributorMapper getContributorMapper() {
		return contributorMapper;
	}

	/**
	 * @param contributorMapper
	 *            the contributorMapper to set
	 */
	public void setContributorMapper(ContributorMapper contributorMapper) {
		this.contributorMapper = contributorMapper;
	}

}
