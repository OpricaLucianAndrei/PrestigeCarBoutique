package it.capstone.prestigecarboutique.repository;


import it.capstone.prestigecarboutique.entity.Auto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AutoRepository extends JpaRepository<Auto,Integer> {
    public List<Auto> findByMarca(String marca);

    public List<Auto> findByModello(String modello);

    public List<Auto> findByAnno(int anno);

    @Query("SELECT a FROM Auto a WHERE a.prezzo.prezzoTotale BETWEEN :prezzoMin AND :prezzoMax")
    public List<Auto> findByIntervalloPrezzo(@Param("prezzoMin") double prezzoMin, @Param("prezzoMax") double prezzoMax);

    public List<Auto> findAutoByChilometraggio(int chilometraggio);

    public List<Auto> findBySpecifiche_Esterni_OpzioniColoriContaining(String opzioneColore);

    public List<Auto> findBySpecifiche_Performance_Motore_Tipo(String tipoMotore);

    public List<Auto> findBySpecifiche_Performance_TopSpeedBetween(String minSpeed, String maxSpeed);

    public List<Auto> findBySpecifiche_Performance_Motore_CavalliBetween(int minCavalli, int maxCavalli);

    public List<Auto> findBySpecifiche_Sicurezza_AssistenzaAllaGuidaContaining(String caratteristiche);

    public List<Auto> findBySpecifiche_Sicurezza_SicurezzaContaining(String caratteristiche);

    public List<Auto> findBySpecifiche_Interni_Posti(int posti);

    public List<Auto> findBySpecifiche_Interni_Tappezzeria(String tappezzeria);

    public List<Auto> findBySpecifiche_Interni_CaratteristicheContaining(String caratteristiche);

    public List<Auto> findBySpecifiche_Esterni_CaratteristicheContaining(String caratteristiche);

    public List<Auto> findBySpecifiche_Dimensioni_LunghezzaBetween(String minLunghezza, String maxLunghezza);

    public List<Auto> findBySpecifiche_Dimensioni_LarghezzaBetween(String minLarghezza, String maxLarghezza);

    public List<Auto> findByMarcaAndModello(String marca, String modello);

    public List<Auto> findByMarcaAndModelloAndAnno(String marca, String modello, int anno);


}
