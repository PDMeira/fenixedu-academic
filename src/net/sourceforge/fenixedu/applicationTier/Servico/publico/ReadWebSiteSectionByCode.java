package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quit�rio 03/10/2003
 * 
 */
public class ReadWebSiteSectionByCode extends Service {

	public Object run(Integer sectionCode) throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		WebSiteSection webSiteSection;
		InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();

		webSiteSection = rootDomainObject.readWebSiteSectionByOID(sectionCode);

		if (webSiteSection == null) {
			throw new NonExistingServiceException();
		}
		
		List<WebSiteItem> webSiteItems = webSiteSection.getPublishedWebSiteItems();  

		if (webSiteItems == null || webSiteItems.size() == 0) {
			throw new NonExistingServiceException();
		}

		// get items with valid dates of publishment
		CollectionUtils.filter(webSiteItems, new Predicate() {
			public boolean evaluate(Object arg0) {
				WebSiteItem webSiteItem = (WebSiteItem) arg0;
				if (!webSiteItem.getOnlineBeginDay().after(Calendar.getInstance().getTime())
						&& !webSiteItem.getOnlineEndDay().before(Calendar.getInstance().getTime())) {
					return true;
				}

				return false;
			}
		});
		if (webSiteItems.size() == 0) {
			throw new NonExistingServiceException();
		}

		List<InfoWebSiteItem> infoWebSiteItems = (List) CollectionUtils.collect(webSiteItems, new Transformer() {
			public Object transform(Object arg0) {
				WebSiteItem webSiteItem = (WebSiteItem) arg0;
				InfoWebSiteItem infoWebSiteItem = InfoWebSiteItem.newInfoFromDomain(webSiteItem);

				return infoWebSiteItem;
			}
		});

		Collections.sort(infoWebSiteItems, new BeanComparator("creationDate"));
		if (webSiteSection.getSortingOrder().equals("descendent")) {
			Collections.reverse(infoWebSiteItems);
		}

		infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

		infoWebSiteSection.setIdInternal(webSiteSection.getIdInternal());
		infoWebSiteSection.setExcerptSize(webSiteSection.getExcerptSize());
		infoWebSiteSection.setInfoWebSite(InfoWebSite.newInfoFromDomain(webSiteSection.getWebSite()));
		infoWebSiteSection.setName(webSiteSection.getName());
		infoWebSiteSection.setSize(webSiteSection.getSize());
		infoWebSiteSection.setSortingOrder(webSiteSection.getSortingOrder());

		return infoWebSiteSection;
	}
}