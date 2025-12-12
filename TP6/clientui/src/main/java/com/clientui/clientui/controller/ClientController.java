package com.clientui.controller;

import com.clientui.beans.CommandeBean;
import com.clientui.beans.PaiementBean;
import com.clientui.beans.ProductBean;
import com.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.proxies.MicroservicePaiementProxy;
import com.clientui.proxies.MicroserviceProduitsProxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceProduitsProxy produitsProxy;

    @Autowired
    private MicroserviceCommandeProxy commandesProxy;

    @Autowired
    private MicroservicePaiementProxy paiementProxy;


    /**
     * Étape 1 :
     * Récupère la liste des produits via ProduitsProxy,
     * et l'envoie à la page Accueil.html.
     */
    @RequestMapping("/")
    public String accueil(Model model) {

        List<ProductBean> produits = produitsProxy.listeDesProduits();
        model.addAttribute("produits", produits);

        return "Accueil";
    }


    /**
     * Étape 2 :
     * Affiche le détail d’un produit sélectionné.
     * Les données sont envoyées à FicheProduit.html.
     */
    @RequestMapping("/details-produit/{id}")
    public String detailsProduit(@PathVariable int id, Model model) {

        ProductBean produit = produitsProxy.recupererUnProduit(id);
        model.addAttribute("produit", produit);

        return "FicheProduit";
    }


    /**
     * Étapes 3 & 4 :
     * - Création d'une commande via le microservice Commandes
     * - Récupération des détails de la commande créée
     * - Envoi des données à la page Paiement.html
     */
    @RequestMapping("/commander-produit/{idProduit}/{montant}")
    public String passerCommande(@PathVariable int idProduit,
                                 @PathVariable Double montant,
                                 Model model) {

        CommandeBean commande = new CommandeBean();
        commande.setProductId(idProduit);
        commande.setQuantite(1);
        commande.setDateCommande(new Date());

        // Appel du MS Commandes via Feign
        CommandeBean commandeCreee = commandesProxy.ajouterCommande(commande);

        model.addAttribute("commande", commandeCreee);
        model.addAttribute("montant", montant);

        return "Paiement";
    }


    /**
     * Étape 5 :
     * Effectue le paiement de la commande via MS Paiement.
     * Vérifie si le paiement est accepté (Status 201).
     */
    @RequestMapping("/payer-commande/{idCommande}/{montantCommande}")
    public String payerCommande(@PathVariable int idCommande,
                                @PathVariable Double montantCommande,
                                Model model) {

        PaiementBean paiementAExecuter = new PaiementBean();
        paiementAExecuter.setIdCommande(idCommande);
        paiementAExecuter.setMontant(montantCommande);

        // Génération d’un faux numéro de carte bancaire
        paiementAExecuter.setNumeroCarte(genererNumeroCarte());

        // Appel du MS Paiement via Feign
        ResponseEntity<PaiementBean> paiement = paiementProxy.payerUneCommande(paiementAExecuter);

        boolean paiementAccepte = paiement.getStatusCode() == HttpStatus.CREATED;

        model.addAttribute("paiementOk", paiementAccepte);

        return "confirmation";
    }


    /**
     * Génère un numéro de carte bancaire fictif (16 chiffres).
     */
    private Long genererNumeroCarte() {
        return ThreadLocalRandom.current().nextLong(1000000000000000L, 9999999999999999L);
    }
}
