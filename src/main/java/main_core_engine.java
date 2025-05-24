import java.io.File;
import java.io.IOException;

//  git restore .
import java.io.*;
import java.util.HashMap;
import java.util.Map;
public class main_core_engine {
    public static void main(String[] args) throws IOException {
        main_core_engine pipeline = new main_core_engine();
        // Initializations
        File mappingFile = new File("./src/main/resources/mini_project_inputs/mapping_file.ttl");
        Writer output = new FileWriter("./src/main/resources/mini_project_inputs/output_file.ttl");
        String outputFilePath = "./src/main/resources/mini_project_inputs/output_file.ttl";
//        String owlFilePath = "./src/main/resources/mini_project_inputs/ontology_initial.owl";
        String owlFilePath = "./src/main/resources/mini_project_inputs/ontology.owl";
        String[] sparqlQueries = new String[6];
        String[] query_descriptions = new String[6];


        query_descriptions[0] = "Find the gdp of top 3 countries with the biggest number of restaurants with 3 michelin stars.";
        query_descriptions[1] = "Find the 3 most well-rated restaurants types.";
        query_descriptions[2] = "Find the michelin stars of the most well-rated restaurant names.";
        query_descriptions[3] = "Find the country name with the biggest number of employees and country's capital city.";
        query_descriptions[4] = "Find the lead of the country which has restaurants with Micchelin.";
        query_descriptions[5] = "Find the supplier of the worst-rated restaurants.";
//        query_descriptions[6] = "Properties question";
//        query_descriptions[7] = "Properties question";



        sparqlQueries[0] = "SELECT DISTINCT ?country_name (COUNT(?restaurant) AS ?three_star_restaurant_count) ?gdpRank\n" +
                "WHERE {\n" +
                "  ?restaurant a <http://purl.obolibrary.org/obo/Restaurant> ;\n" +
                "              ex:country_name ?country_name ;\n" +
                "              ex:michelin_star_number 3 .\n" +
                "  BIND(IRI(CONCAT(\"http://dbpedia.org/resource/\", REPLACE(?country_name, \" \", \"_\"))) AS ?dbpediaCountry)\n" +
                "  SERVICE <https://dbpedia.org/sparql> {\n" +
                "    ?dbpediaCountry dbp:gdpNominalRank ?gdpRank .\n" +
                "  }\n" +
                "}\n" +
                "GROUP BY ?country_name ?gdpRank\n" +
                "ORDER BY DESC(?three_star_restaurant_count)\n" +
                "LIMIT 3"; // I = 1





        sparqlQueries[1] = "SELECT ?cuisine_type (AVG(?rating) AS ?average_rating)\n" +
                "WHERE {\n" +
                "  ?restaurant a <http://purl.obolibrary.org/obo/Restaurant> ;\n" +
                "              dcterms:type ?cuisine_type ;\n" +
                "              ex:rating ?rating .\n" +
                "}\n" +
                "GROUP BY ?cuisine_type\n" +
                "ORDER BY DESC(?average_rating)"; // I = 2


        sparqlQueries[2] = "SELECT ?restaurant_name ?michelin_stars ?rating\n" +
                "WHERE {\n" +
                "  ?restaurant a <http://purl.obolibrary.org/obo/Restaurant> ;\n" +
                "              ex:michelin_star_number ?michelin_stars ;\n" +
                "              ex:rating ?rating ;\n" +
                "              rdfs:label ?restaurant_name .\n" +
                "}\n" +
                "ORDER BY DESC(?rating)"; // I = 3
        sparqlQueries[3] = "SELECT ?country_name (SUM(?num_employees) AS ?total_employees) ?capital_name\n" +
                "WHERE {\n" +
                "  ?restaurant a <http://purl.obolibrary.org/obo/Restaurant> ;\n" +
                "              ex:country_name ?country_name ;\n" +
                "              ex:employees ?num_employees .\n" +
                "  BIND(IRI(CONCAT(\"http://dbpedia.org/resource/\", REPLACE(?country_name, \" \", \"_\"))) AS ?dbpediaCountry)\n" +
                "  SERVICE <https://dbpedia.org/sparql> {\n" +
                "    ?dbpediaCountry dbp:capital ?capital_name .\n" +
                "  }\n" +
                "}\n" +
                "GROUP BY ?country_name ?capital_name\n" +
                "ORDER BY DESC(?total_employees)\n" +
                "LIMIT 10"; // I = 4


        sparqlQueries[4] = "SELECT ?restaurant_name ?supplier\n" +
                "WHERE {\n" +
                "  ?restaurant a obo:Restaurant ;\n" +
                "              rdfs:label ?restaurant_name ;\n" +
                "              ex:Supplier ?supplier ;\n" +
                "              ex:rating ?rating .\n" +
                "}\n" +
                "ORDER BY ASC(xsd:float(?rating))\n" +
                "LIMIT 5"; // I = 5
        sparqlQueries[5] = "SELECT DISTINCT ?restaurant_name ?country_name ?leader_name\n" +
                "WHERE {\n" +
                "  ?restaurant a obo:Restaurant ;\n" +
                "              ex:country_name ?country_name ;\n" +
                "              ex:michelin_star_number 1 ;\n" +
                "              rdfs:label ?restaurant_name .\n" +
                "  BIND(IRI(CONCAT(\"http://dbpedia.org/resource/\", REPLACE(?country_name, \" \", \"_\"))) AS ?dbpediaCountry)\n" +
                "  SERVICE <https://dbpedia.org/sparql> {\n" +
                "    OPTIONAL {\n" +
                "      ?dbpediaCountry dbp:leaderName ?leader_name .\n" +
                "    }\n" +
                "  }   \n" +
                "}\n" +
                "LIMIT 6"; // I = 6 enter some cook name with cook and add some specialite for
//        sparqlQueries[6] ="write something";
//        sparqlQueries[7] = "write something";



        RML_pipeline.runRMLMapper(mappingFile,output);
//        GRAPH_DB_manager.store_RDF_to_Graph(outputFilePath,owlFilePath);
//        System.out.println("starting the querrry process");
//        for (int i = 0; i < sparqlQueries.length; i++) {
//            System.out.println("-------------------------- Initialize starting the querrry process------------------------------------------------------------");
//            GRAPH_DB_manager.execute_sparql_queries(sparqlQueries[i],query_descriptions[i]);
//        }


    }
}
