/*
 * Created on 25/Ago/2003, 15:09:58
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.ICaseStudyChoice;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCandidacy;
import ServidorPersistente.Seminaries.IPersistentSeminaryCaseStudyChoice;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 25/Ago/2003, 15:09:58
 *  
 */
public class DeleteCandidacy implements IService {

    public DeleteCandidacy() {
    }

    public void run(Integer id) throws BDException {
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminaryCandidacy persistentCandidacy = persistenceSupport
                    .getIPersistentSeminaryCandidacy();
            IPersistentSeminaryCaseStudyChoice persistentChoice = persistenceSupport
                    .getIPersistentSeminaryCaseStudyChoice();
            ICandidacy candidacy = (ICandidacy) persistentCandidacy.readByOID(Candidacy.class, id);
            List choices = candidacy.getCaseStudyChoices();
            for (Iterator iterator = choices.iterator(); iterator.hasNext();) {
                ICaseStudyChoice choice = (ICaseStudyChoice) iterator.next();
                persistentChoice.delete(choice);
            }
            persistentCandidacy.deleteByOID(Candidacy.class, id);
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException("Got an error while trying to delete a candidacy from the database",
                    ex);
        }
    }

}