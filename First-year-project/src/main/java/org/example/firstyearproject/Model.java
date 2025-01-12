package org.example.firstyearproject;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.example.firstyearproject.AdressSearch.Address;
import org.example.firstyearproject.AdressSearch.Trie;
import org.example.firstyearproject.KdTree.KdTree;
import org.example.firstyearproject.ShortestPath.DirectedEdge;
import org.example.firstyearproject.ShortestPath.EdgeWeightedDigraph;
import org.example.firstyearproject.KdTree.Point2D;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;


public class Model implements Serializable {

    KdTree kdTree;

    KdTree KdHouseTree;
    List<Way> ways = new ArrayList<Way>();

    HashMap<Long, Node> id2node = new HashMap<Long, Node>();

    List<Relation> relations = new ArrayList<Relation>();


    Map<Long, List<Long>> wayNodesMap = new HashMap<>();
    EdgeWeightedDigraph G;
    EdgeWeightedDigraph GCycle;

    List<String> address = new ArrayList<>();

    public Trie trie;

    HashMap<String, Node> addressXnode = new HashMap<>();

    double minlat, maxlat, minlon, maxlon;
    ArrayList<Node> wayNodes = new ArrayList<>();

    static Model load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }


    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (filename.endsWith(".osm.zip") || filename.endsWith(".zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        } else {
            throw new IllegalArgumentException("Unknown file type: " + filename);
        }
        //save(filename + ".obj");
    }

    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        System.out.println("Attempting to parse ZIP file using Apache Commons Compress: " + filename);
        File file = new File(filename);
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                System.out.println("Processing entry: " + entry.getName());
                if (!entry.isDirectory() && entry.getName().endsWith(".osm")) {
                    try (InputStream inputStream = zipFile.getInputStream(entry)) {
                        parseOSM(inputStream);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to parse ZIP file: " + e.getMessage());
            throw e;
        }
    }

    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseOSM(new FileInputStream(filename));
    }

    private void parseOSM(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));


        var id2way = new HashMap<Long, Way>();
        String wayType = "";
        Set<String> wayTypes = new HashSet<String>();
        var outer = new ArrayList<Way>();
        var inner = new ArrayList<Way>();

        Long currentRelation = null;
        var id = 0L;
        var natural = "";
        var landuse = "";
        var RelationType = "";
        int speedlimit = 0;

        boolean bc = true;


        String street = "";
        String houseNumber = "";
        String postcode = "";
        String city = "";
        String country = "";
        Boolean building = false;


        double lat = 0;
        double lon = 0;

        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                switch (name) {
                    case "bounds":
                        minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
                        maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
                        minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
                        maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
                        break;
                    case "node":
                        id = Long.parseLong(input.getAttributeValue(null, "id"));
                        lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                        lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                        street = "";
                        houseNumber = "";
                        postcode = "";
                        city = "";
                        country = "";
                        break;
                    case "way":
                        wayNodes = new ArrayList<>();
                        wayType = "";
                        natural = "";
                        landuse = "";
                        id = Long.parseLong(input.getAttributeValue(null, "id"));
                        building = false;

                        break;
                    case "tag":
                        var v = input.getAttributeValue(null, "v");
                        var k = input.getAttributeValue(null, "k");
                        switch (k) {
                            case "power", "cables":
                                continue;
                            case "place":
                                if (v.equals("islet")) {
                                    break;
                                }
                                break;
                            case "maxspeed":
                                try {
                                    speedlimit = Integer.parseInt(v);
                                } catch (NumberFormatException e) {
                                    speedlimit = 50;
                                }
                                break;
                            case "highway":
                                wayType = v;
                                wayTypes.add(v);
                                for (Node ref : wayNodes) {
                                    if (bc) {
                                        ref.isbicycle();
                                    }
                                    wayNodesMap.computeIfAbsent(id, j -> new ArrayList<>()).add(ref.getID());
                                }
                                break;
                            case "natural":
                                natural = v;
                                break;
                            case "type":
                                RelationType = v;
                                break;
                            case "oneway":
                                for (Node ref : wayNodes) {
                                    ref.setOneWay();
                                }
                                break;
                            case "bicycle":
                                if (v.equals("no")) {
                                    bc = false;
                                }
                                break;
                            case "addr:street":
                                street = v;
                                break;
                            case "addr:housenumber":
                                houseNumber = v;
                                break;
                            case "addr:postcode":
                                postcode = v;
                                break;
                            case "addr:city":
                                city = v;
                                break;
                            case "addr:country":
                                country = v;
                                break;
                            case "boundary":
                                if (v.equals("administrative")) {
                                    break;
                                }
                                break;
                            case "building":
                                if (v.equals("yes")) {
                                    building = true;
                                }
                                break;
                            case "landuse":
                                landuse = v;
                                break;
                        }

                        address.add(new Address(street, houseNumber, postcode, city, country).toString());
                        break;
                    case "nd":
                        var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                        var node = id2node.get(ref);
                        wayNodes.add(node);
                        break;
                    case "relation":
                        outer.clear();
                        inner.clear();
                        building = false;
                        natural = "";
                        landuse = "";
                        currentRelation = Long.parseLong(input.getAttributeValue(null, "id"));
                        break;
                    case "member":
                        var memberType = input.getAttributeValue(null, "type");
                        ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                        var role = input.getAttributeValue(null, "role");
                        if (memberType.equals("way") && role.equals("outer")) {
                            var w = id2way.get(ref);
                            if (w != null) {
                                outer.add(w);
                            }
                        } if (memberType.equals("way") && role.equals("inner")) {
                            var w = id2way.get(ref);
                            if (w != null) {
                                inner.add(w);
                            }
                        }
                        break;
                }

            }
            if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
                switch (name) {
                    case "way":
                        Way w;
                        if (speedlimit == 0) {
                            if (natural.isBlank() && landuse.isBlank()) {
                                w = new Way(new ArrayList<>(wayNodes), wayType);
                                ways.add(w);
                            } else {
                                w = new Way(new ArrayList<>(wayNodes), wayType, speedlimit, natural, landuse);
                                ways.add(w);
                            };
                        } else {
                            if (natural.isBlank() && landuse.isBlank()) {
                                w = new Way(new ArrayList<>(wayNodes), wayType, speedlimit);
                                ways.add(w);
                                if (building) {
                                    w.isBuilding();
                                }
                            } else {
                                w = new Way(new ArrayList<>(wayNodes), wayType, speedlimit, natural, landuse);
                                ways.add(w);
                                if (building) {
                                    w.isBuilding();
                                }
                            }
                        }
                        id2way.put(id, w);

                        int j = 1;
                        bc = true;
                        break;
                    case "relation":
                        if (currentRelation != null) {
                            if (natural.isBlank() && landuse.isBlank()) {
                                Relation r = new Relation(outer, inner, RelationType);
                                relations.add(r);
                                if (building) {
                                    r.isBuilding();
                                }
                            } else {
                                Relation r = new Relation(outer, inner, RelationType, natural, landuse);
                                relations.add(r);
                                if (building) {
                                    r.isBuilding();
                                }
                            }
                        }
                        break;
                    case "node":
                        Node n = new Node(lat, lon, id);
                        addressXnode.put(new Address(street, houseNumber, postcode, city, country).toString().toLowerCase(), n);
                        id2node.put(id, n);
                        break;
                }

            }
        }

        ArrayList<DirectedEdge> edges = new ArrayList<>();
        int slimit;
        for (Long wayId : wayNodesMap.keySet()) {
            Way w = id2way.get(wayId);
            slimit = w.getSpeedlimit();
            List<Long> nodeIds = wayNodesMap.get(wayId);
            for (int i = 0; i < nodeIds.size() - 1; i++) {
                Long nodeId1 = nodeIds.get(i);
                Long nodeId2 = nodeIds.get(i + 1);
                Node node1 = id2node.get(nodeId1);
                Node node2 = id2node.get(nodeId2);
                double weight = (cordToKM(node1, node2) / slimit) * 60;
                if (node1.isOneWay()) {

                    DirectedEdge edge = new DirectedEdge(nodeId1, nodeId2, weight);
                    edges.add(edge);

                } else {
                    DirectedEdge edge = new DirectedEdge(nodeId1, nodeId2, weight);
                    DirectedEdge edge2 = new DirectedEdge(nodeId2, nodeId1, weight);
                    edges.add(edge);
                    edges.add(edge2);
                }
            }
        }
        GCycle = new EdgeWeightedDigraph(edges.size());

        G = new EdgeWeightedDigraph(edges.size());

        for (DirectedEdge edge : edges) {
            G.addEdge(edge);
            if (id2node.get(edge.from()).getBicycle() && id2node.get(edge.to()).getBicycle()) {
                GCycle.addEdge(edge);

            }
        }

        kdTree = new KdTree(-maxlat, 0.56 * minlon, -minlat, 0.56 * maxlon);
        KdHouseTree = new KdTree(-maxlat, 0.56 * minlon, -minlat, 0.56 * maxlon);


        for (Way w : ways) {
            if (!w.wayType.isBlank()){  //FIND UD AF HVORFOR WAY KAN VÆRE NULL
                int i = w.nodes.size() / 2;
                Node n = w.nodes.get(i);
                Point2D j = new Point2D(n.getX(), n.getY(), w);
                kdTree.insert(j);
            } else {
                int i = w.nodes.size() / 2;
                Node n = w.nodes.get(i);
                Point2D j = new Point2D(n.getX(), n.getY(), w);
                KdHouseTree.insert(j);
            }
        }

        trie = new Trie(address);

    }


    double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    double cordToKM(Node n1, Node n2) {
        double startLat = n1.getLat();
        double startLong = n1.getLong();
        double endLat = n2.getLat();
        double endLong = n2.getLong();

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }
}

