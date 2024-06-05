package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;

import java.util.*;

public class DecisionTree implements Cloneable{

    private Node root;
    private List<DecisionTreeClass> classes;
    private StringBuilder text;

    public DecisionTree() {
        this.root = new Node();
        this.classes = new ArrayList<DecisionTreeClass>();
        this.text = new StringBuilder();
    }

    public DecisionTree(Node root, List<DecisionTreeClass> classes) {
        this.root = root;
        this.classes = classes;
        this.text = new StringBuilder();
    }

    public void calculateNp(List<Instance> data, List<Attribute> attributes) {
        for (DecisionTreeClass cla : classes) {
            cla.resetCount();
        }

        for (Attribute att : attributes) {
            for (Branch b : att.getBranches()) {
                b.resetCount(classes.size());
            }
        }

        for (Instance in : data) {
            for (String valeurAttribute : in.getAttributeValues()) {
                for (Attribute att : attributes) {
                    for (Branch b : att.getBranches()) {
                        for (DecisionTreeClass cla : classes) {
                            if (valeurAttribute.equals(b.getValue()) && in.getClassLabel().equals(cla.getClassName())) {
                                b.setInstanceIds(addInstanceId(b.getInstanceIds(), in.getInstanceId()));
                                b.setTotalInstanceCount(b.getTotalInstanceCount() + 1);
                                int tempidclass = cla.getClassId();
                                int tempnbApparicientClasse[] = b.getInstanceCountsInClasses();
                                tempnbApparicientClasse[tempidclass - 1] = tempnbApparicientClasse[tempidclass - 1] + 1;
                                b.setInstanceCountsInClasses(tempnbApparicientClasse);
                            }
                        }
                    }
                }
            }
            for (DecisionTreeClass cla : classes) {
                if (in.getClassLabel().equals(cla.getClassName())) {
                    cla.setAppearanceCount(cla.getAppearanceCount() + 1);
                }
            }
        }
    }

    public int[] addInstanceId(int[] oldTab, int idInstance) {
        int[] newTab = new int[oldTab.length + 1];
        System.arraycopy(oldTab, 0, newTab, 0, oldTab.length);
        newTab[oldTab.length] = idInstance;
        return newTab;
    }

    public double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public double calculateEntropy(Attribute att) {
        int total = 0;
        for (DecisionTreeClass arb : classes) {
            total += arb.getAppearanceCount();
        }

        double entropy = 0;
        for (Branch b : att.getBranches()) {
            entropy += (double) b.getTotalInstanceCount() / (double) (total) * calculateInformationRate(b.getInstanceCountsInClasses());
        }
        return entropy;
    }

    public double calculateInformationRate(int[] nombreApparitionClasses) {
        int nombreZero = 0;
        int sommeApparition = 0;
        for (int tempNbApparition : nombreApparitionClasses) {
            if (tempNbApparition == 0) {
                nombreZero++;
            }
            sommeApparition += tempNbApparition;
        }
        if (nombreZero == nombreApparitionClasses.length) {
            return 0;
        }

        double informationRate = 0;
        for (int temp : nombreApparitionClasses) {
            if (temp == 0) {
                continue;
            } else {
                double proba = (double) temp / (double) (sommeApparition);
                informationRate += -1 * (proba) * log2(proba);
            }
        }
        return informationRate;
    }

    public double calculateGain(Attribute att) {
        int[] nombreApparitionClasseGlobal = new int[classes.size()];
        int indice = 0;
        for (DecisionTreeClass arb : classes) {
            nombreApparitionClasseGlobal[indice] = arb.getAppearanceCount();
            indice++;
        }

        return calculateInformationRate(nombreApparitionClasseGlobal) - calculateEntropy(att);
    }

    public String calculateMajorityClass(List<Instance> instancesReste) {
        for (DecisionTreeClass cla : classes) {
            cla.setAppearanceCount(0);
        }

        for (Instance in : instancesReste) {
            for (DecisionTreeClass cla : classes) {
                if (in.getClassLabel().equals(cla.getClassName())) {
                    cla.setAppearanceCount(cla.getAppearanceCount() + 1);
                }
            }
        }

        int max = classes.get(0).getAppearanceCount();
        String classMax = classes.get(0).getClassName();
        for (DecisionTreeClass cla : classes) {
            if (cla.getAppearanceCount() > max) {
                max = cla.getAppearanceCount();
                classMax = cla.getClassName();
            }
        }

        return classMax;
    }

    public void id3(List<Attribute> attributes, List<Instance> instances){

        buildTree(root, attributes, instances);
    }
    private  void buildTree(Node currentNode, List<Attribute> attributesRestants, List<Instance> instancesRestantes) {
        calculateNp(instancesRestantes, attributesRestants);

        double maxGain = 0;
        Attribute meilleurAttribute = null;

        for (Attribute a : attributesRestants) {
            double gain = calculateGain(a);
            if (gain > maxGain) {
                maxGain = gain;
                meilleurAttribute = a;
            }
        }

        if (meilleurAttribute == null) {
            currentNode.setLeaf(true);
            currentNode.setMajorClass(calculateMajorityClass(instancesRestantes));
            return;
        }

        Attribute attributeCopie = meilleurAttribute.clone();
        currentNode.setAttribute(attributeCopie);

        for (Branch branche : attributeCopie.getBranches()) {
            List<Instance> instancesPartitionnees = new ArrayList<>();
            List<Attribute> attributesRestantsFils = new ArrayList<>(attributesRestants);
            attributesRestantsFils.remove(attributeCopie);

            for (Instance instance : instancesRestantes) {
                for (int idInstanceBranche : branche.getInstanceIds()) {
                    if (idInstanceBranche == instance.getInstanceId()) {
                        instancesPartitionnees.add(instance);
                    }
                }
            }

            Node noeudFils = new Node();
            branche.setChildNode(noeudFils);

            buildTree(noeudFils, attributesRestantsFils, instancesPartitionnees);
        }
    }

    public String evaluateInstance(Instance instance) {
        Node currentNode = root.clone();

        while (!currentNode.isLeaf()) {
            Attribute attributeCourant = currentNode.getAttribute();
            String valeurAttributeInstance = instance.getSingleAttributeValue(attributeCourant);

            boolean brancheTrouvee = false;
            for (Branch branche : attributeCourant.getBranches()) {
                if (branche.getValue().equals(valeurAttributeInstance)) {
                    brancheTrouvee = true;
                    currentNode = branche.getChildNode();
                    break;
                }
            }

            if (!brancheTrouvee) {
                return currentNode.getMajorClass();
            }
        }

        return currentNode.getMajorClass();
    }

    public int[][] generateConfusionMatrix(List<Instance> dataTest) {
        int[][] matrix = new int[classes.size()][classes.size()];

        for (Instance i : dataTest) {
            String classReel = i.getClassLabel();
            String classPred = evaluateInstance(i);
            int indiceClassReel = 0;
            int indiceClassPred = 0;

            for (DecisionTreeClass arb : classes) {
                if (classReel.equals(arb.getClassName())) {
                    indiceClassReel = arb.getClassId() - 1;
                }
                if (classPred.equals(arb.getClassName())) {
                    indiceClassPred = arb.getClassId() - 1;
                }
            }

            if (classPred.equals(classReel)) {
                matrix[indiceClassReel][indiceClassReel]++;
            } else {
                matrix[indiceClassReel][indiceClassPred]++;
            }
        }
        return matrix;
    }

    public void displayTree(Node currentNode, String indentation) {
        if (currentNode.isLeaf()) {
            System.out.println(indentation + "Leaf: " + currentNode.getMajorClass());
            text.append(indentation).append("Leaf: ").append(currentNode.getMajorClass()).append("\n");
        } else {
            System.out.println(indentation + "Attribute: " + currentNode.getAttribute().getAttributeName());
            text.append(indentation).append("Attribute: ").append(currentNode.getAttribute().getAttributeName()).append("\n");
            for (Branch branche : currentNode.getAttribute().getBranches()) {
                System.out.println(indentation + "  Branch: " + branche.getValue());
                text.append(indentation).append("  Branch: ").append(branche.getValue()).append("\n");
                displayTree(branche.getChildNode(), indentation + "    ");
            }
        }
    }

    public void displayTreeString(Node currentNode, String indentation) {
        if (currentNode.isLeaf()) {
            text.append(indentation).append("Leaf: ").append(currentNode.getMajorClass()).append("\n");
        } else {
            text.append(indentation).append("Attribute: ").append(currentNode.getAttribute().getAttributeName()).append("\n");
            for (Branch branche : currentNode.getAttribute().getBranches()) {
                text.append(indentation).append("  Branch: ").append(branche.getValue()).append("\n");
                displayTreeString(branche.getChildNode(), indentation + "    ");
            }
        }
    }

    public Node getRoot() {
        return root;
    }

    @Override
    public DecisionTree clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (DecisionTree) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}