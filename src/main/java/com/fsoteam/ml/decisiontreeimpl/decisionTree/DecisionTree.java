package com.fsoteam.ml.decisiontreeimpl.decisionTree;

import com.fsoteam.ml.decisiontreeimpl.model.DecisionTreeClass;
import com.fsoteam.ml.decisiontreeimpl.model.Instance;
import com.fsoteam.ml.decisiontreeimpl.utils.LearningModel;

import java.util.*;
import java.util.stream.Collectors;

public abstract class DecisionTree implements Cloneable, LearningModel {

    private Node root;
    private List<DecisionTreeClass> classes;
    private StringBuilder text;
    private List <Attribute> attributes;

    public DecisionTree() {
        this.root = new Node();
        this.classes = new ArrayList<DecisionTreeClass>();
        this.attributes = new ArrayList<Attribute>();
        this.text = new StringBuilder();
    }

    public DecisionTree(Node root, List<DecisionTreeClass> classes, List<Attribute> attributes) {
        super();
        this.root = root;
        this.classes = classes;
        this.attributes = attributes;
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
                                int[] tempnbApparicientClasse = b.getInstanceCountsInClasses();
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
        classes.forEach(cla -> cla.setAppearanceCount(0));

        instancesReste.forEach(in -> classes.stream()
                .filter(cla -> in.getClassLabel().equals(cla.getClassName()))
                .forEach(cla -> cla.setAppearanceCount(cla.getAppearanceCount() + 1)));

        return classes.stream()
                .max(Comparator.comparing(DecisionTreeClass::getAppearanceCount))
                .map(DecisionTreeClass::getClassName)
                .orElse(null);
    }




    @Override
    public String evaluate(Instance instance) {
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

    @Override
    public int[][] generateConfusionMatrix(List<Instance> dataTest) {
        int[][] matrix = new int[classes.size()][classes.size()];

        dataTest.forEach(i -> {
            String classReel = i.getClassLabel();
            String classPred = evaluate(i);

            int indiceClassReel = classes.stream()
                    .filter(arb -> classReel.equals(arb.getClassName()))
                    .mapToInt(DecisionTreeClass::getClassId)
                    .findFirst()
                    .orElse(0) - 1;

            int indiceClassPred = classes.stream()
                    .filter(arb -> classPred.equals(arb.getClassName()))
                    .mapToInt(DecisionTreeClass::getClassId)
                    .findFirst()
                    .orElse(0) - 1;

            if (classPred.equals(classReel)) {
                matrix[indiceClassReel][indiceClassReel]++;
            } else {
                matrix[indiceClassReel][indiceClassPred]++;
            }
        });

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

    public List<DecisionTreeClass> getClasses() {
        return classes;
    }

    public List<Attribute> getAttributes() {
        return attributes;
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
    @Override
    public LearningModel cloneModel() {
        return this.clone();
    }
}