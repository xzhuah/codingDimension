package common.framework;

import java.util.*;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/12/7, 21:15
 * common.framework in codingDimensionTemplate
 */
public class BaseModelProvider {

    private Map<Class, List> memory;

    public BaseModelProvider() {
        memory = new HashMap<>();
        this.configure();
    }

    protected void install(BaseModelProvider otherModelProvider) {
        for (Class clazz : otherModelProvider.memory.keySet()) {
            this.memory.put(clazz, otherModelProvider.memory.get(clazz));
            this.memory.get(clazz).clear();
        }
    }

    protected <T> void register(Class<T> clazz) {
        memory.put(clazz, new ArrayList<T>());
    }

    protected void configure() {
    }

    public <T> boolean hasModel(Class<T> clazz) {
        return memory.containsKey(clazz);
    }

    public <T> Optional<T> getModel(Class<T> clazz) {
        if (!hasModel(clazz)) {
            return Optional.empty();
        }
        checkStatus(memory.get(clazz).size() == 1, String.format("Can't use this method when you have %s object", memory.get(clazz).size()));
        return Optional.of((T)memory.get(clazz).get(0));
    }

    public <T> Optional<List<T>> getAllModel(Class<T> clazz) {
        if (!hasModel(clazz)) {
            return Optional.empty();
        }
        return Optional.of(memory.get(clazz));
    }

    public <T> void setModel(T model, Class<T> clazz) {
        checkStatus(hasModel(clazz), String.format("%s is not register in this ModelProvider", clazz.getName()));
        memory.get(clazz).clear();
        memory.get(clazz).add(model);
    }

    public <T> void addModel(T model, Class<T> clazz) {
        checkStatus(hasModel(clazz), String.format("%s is not register in this ModelProvider", clazz.getName()));
        memory.get(clazz).add(model);
    }

    public <T> void addAllModel(List<T> models, Class<T> clazz) {
        checkStatus(hasModel(clazz), String.format("%s is not register in this ModelProvider", clazz.getName()));
        memory.get(clazz).addAll(models);
    }

    public <T> void setAllModel(List<T> models, Class<T> clazz) {
        checkStatus(hasModel(clazz), String.format("%s is not register in this ModelProvider", clazz.getName()));
        memory.get(clazz).clear();
        memory.get(clazz).addAll(models);
    }


    public static <T, U extends BaseModelProvider> U toModelProvider(T model, Class<T> clazz, Class<U> provideClazz) {
        try {
            U result = provideClazz.getDeclaredConstructor().newInstance();
            result.setModel(model, clazz);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, U extends BaseModelProvider> U toModelProvider(List<T> models, Class<T> clazz, Class<U> provideClazz) {
        try {
            U result = provideClazz.getDeclaredConstructor().newInstance();
            result.setAllModel(models, clazz);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
