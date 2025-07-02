package io.github.kelari.atg.report;

public class ReportGenerator {

    private ListenerEvidenceRecorder evidenceRecorder;

    public ReportGenerator(ListenerEvidenceRecorder evidenceRecorder) {
        this.evidenceRecorder = evidenceRecorder;
    }

    /*
    *  public void flushToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File output = new File("target/kelari-evidences/evidences.json");
        output.getParentFile().mkdirs();
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, evidences);
    }
    //kelari-doc/test-coverage
    public void clear() {
        evidences.clear();
    }
    * */
}
