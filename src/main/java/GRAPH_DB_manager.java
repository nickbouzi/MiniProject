import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.rio.RDFFormat;
import java.io.*;

public class GRAPH_DB_manager {
    private static final String PREFIXES = "PREFIX ex: <http://www.examples.org/restaurants#>\n"
            + "PREFIX dcterms: <http://purl.org/dc/terms/>\n"
            + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
            + "PREFIX obo: <http://purl.obolibrary.org/obo/>"
            + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
            + "PREFIX dbr: <http://dbpedia.org/resource/>\n"
            + "PREFIX dbp: <http://dbpedia.org/property/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n";
    public static void store_RDF_to_Graph(String ontology_path, String outputFile_path) {
        String repositoryName = "MiniProject";
        HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/" + repositoryName);
        RepositoryConnection connection = repository.getConnection();
        // Clear the repository before we start
        connection.clear();
        // load a simple ontology from a file
        connection.begin();
        try {
            connection.add(new FileInputStream(ontology_path), // ΥOU NEED THE ONTOLGO
                    "urn:base",
                    RDFFormat.TURTLE);
            connection.add(new FileInputStream(outputFile_path),
                    "urn:base",
                    RDFFormat.TURTLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.commit();
        connection.close();

        repository.shutDown();
    }
    public static void execute_sparql_queries(String sparql_query ,String query_description) {
        String repositoryName = "MiniProject";
        HTTPRepository repository = new HTTPRepository("http://localhost:7200/repositories/" + repositoryName);
        RepositoryConnection connection = repository.getConnection();

        try {
            connection.begin();

            String query_sparql = PREFIXES + sparql_query;
            TupleQuery tupleQuery_SPARQ = connection.prepareTupleQuery(query_sparql);
            TupleQueryResult results = tupleQuery_SPARQ.evaluate();

            System.out.println("---- " + query_description + " ----");

            while (results.hasNext()) {
                BindingSet result = results.next();  // One row of results
                for (String bindingName : result.getBindingNames()) {
                    System.out.println(bindingName + " => " + result.getValue(bindingName));
                }
                System.out.println("----");
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.close();
            repository.shutDown();
        }
    }
}



