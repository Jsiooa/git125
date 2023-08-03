package org.ycframework.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.ycframework.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class YcAnnotationConfigApplicationContext implements YcApplicationContext {
    private Logger logger = LoggerFactory.getLogger(YcAnnotationConfigApplicationContext.class);
    private Map<String, YcBeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, Object> beanMap = new HashMap<>();
    private Properties pros;

    public YcAnnotationConfigApplicationContext(Class... configClasses) {
        try {
            pros = System.getProperties();
            List<String> toScanPackagePath = new ArrayList<>();
            for (Class cls : configClasses) {
                if (cls.isAnnotationPresent(YcConfiguration.class) == false) {
                    continue;
                }
                String[] basePakages=null;
                if (cls.isAnnotationPresent(YcComponentScan.class)) {
                    YcComponentScan ycComponentScan = (YcComponentScan) cls.getAnnotation(YcComponentScan.class);
                    basePakages = ycComponentScan.basePackages();
                    if (basePakages == null || basePakages.length <= 0) {
                        basePakages = new String[1];
                        basePakages[0] = cls.getPackage().getName();
                    }
                    logger.info(cls.getName() + "类上有@YcComponentScan注解 他要扫描的路径:" + basePakages[0]);
                }
                recursiveLoadBeanDefinition(basePakages);
            }
            createBean();
            doDi();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

    private void doDi() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (Map.Entry<String,Object> entry:beanMap.entrySet()){
            String beanId=entry.getKey();
            Object beanObj=entry.getValue();
            Field[] fields=beanObj.getClass().getDeclaredFields();
            for (Field field:fields){
                if (field.isAnnotationPresent(YcResource.class)){
                    YcResource ycResource=field.getAnnotation(YcResource.class);
                    String toDiBeanId=ycResource.name();
                    Object obj=getToDiBean(toDiBeanId);
                    field.setAccessible(true);
                    field.set(beanObj,obj);
                }
            }
        }

    }

    private Object getToDiBean(String toDiBeanId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (beanMap.containsKey(toDiBeanId)){
            return beanMap.get(toDiBeanId);
        }
        if (!beanDefinitionMap.containsKey(toDiBeanId)){
            throw new RuntimeException("spring容器中没有加载此class:"+toDiBeanId);
        }
        YcBeanDefinition bd=beanDefinitionMap.get(toDiBeanId);
        if (bd.isLazy()){
            String classpath=bd.getClassInfo();
            Object beanObj=Class.forName(classpath).newInstance();
            beanMap.put(toDiBeanId,beanObj);
            return beanObj;
        }
        if (bd.getScope().equalsIgnoreCase("prototype")){
            String classpath=bd.getClassInfo();
            Object beanObj=Class.forName(classpath).newInstance();
            return beanObj;
        }
        return null;
    }

    /**
     * 创建bean（是否为懒加载，是单例)，存到beanMap
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void createBean() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Map.Entry<String, YcBeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanId = entry.getKey();
            YcBeanDefinition ybd = entry.getValue();
            if (!ybd.isLazy() && !ybd.getScope().equalsIgnoreCase("prototypes")) {
                String classInfo = ybd.getClassInfo();
                Object obj = Class.forName(classInfo).newInstance();
                beanMap.put(beanId, obj);
                logger.trace("spring容器托管了:" + beanId + "=>" + classInfo);
            }

        }

    }

    private void recursiveLoadBeanDefinition(String[] basePackages) {
        for (String basePackage : basePackages) {
            // 将包名中的. 替换成路径中的/
            String packagePath = basePackage.replaceAll("\\.", "/");

            Enumeration<URL> files = null;
            try {
                files = Thread.currentThread().getContextClassLoader().getResources(packagePath);
                //循环这个files，看是否是我要加载的资源
                while (files.hasMoreElements()) {
                    URL url = files.nextElement();
                    logger.trace("当前正在递归加载:" + url.getFile());
                    //查找此包下的类    com/yc全路径   com/yc包名
                    findPackageClasses(url.getFile(), basePackage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void findPackageClasses(String packagePath, String basePackage) {

        if (packagePath.startsWith("/")) {
            packagePath = packagePath.substring(1);
            File file = new File(packagePath);


            File[] classFiles = file.listFiles((pathname) -> {
                if (pathname.getName().endsWith(".class") || pathname.isDirectory()) {
                    return true;
                }
                return false;
            });

            if (classFiles == null || classFiles.length <= 0) {
                return;
            }
            for (File cf : classFiles) {
                if (cf.isDirectory()) {
                    //继续递归
                    logger.trace("递归:" + cf.getAbsolutePath() + "，它对应的包名为:" + (basePackage + "," + cf.getName()));
                    findPackageClasses(cf.getAbsolutePath(), basePackage + "," + cf.getName());
                } else {
                    //是class文件，则取出文件，判断此文件对应的class中是否有l@Component注解
                    URLClassLoader uc = new URLClassLoader(new URL[]{});
                    Class cls = null;
                    try {
                        cls = uc.loadClass(basePackage + "." + cf.getName().replaceAll(".class", ""));
                        //todo: 可以支持YcComponent子注解
                        if (cls.isAnnotationPresent(YcComponent.class)
                                || cls.isAnnotationPresent(YcConfiguration.class)
                                || cls.isAnnotationPresent(YcController.class)
                                || cls.isAnnotationPresent(YcRepository.class)
                                || cls.isAnnotationPresent(YcService.class)) {

                            logger.info("加载到一个待托管的类:" + cls.getName());
                            //todo: 包装成BeanDefinition
                            YcBeanDefinition bd=new YcBeanDefinition();
                            if (cls.isAnnotationPresent(YcLazy.class)){
                                bd.setLazy(true);
                            }
                            if (cls.isAnnotationPresent(YcScope.class)){
                                YcScope ycScope= (YcScope) cls.getAnnotation(YcScope.class);
                                String scope=ycScope.value();
                                bd.setScope(scope);
                            }
                            bd.setClassInfo(basePackage+"."+cf.getName().replaceAll(".class",""));
                            String beanId=getBeanId(cls);
                            this.beanDefinitionMap.put(beanId,bd);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getBeanId(Class cls){
        YcComponent ycComponent= (YcComponent) cls.getAnnotation(YcComponent.class);
        YcController ycController= (YcController) cls.getAnnotation(YcController.class);
        YcService ycService= (YcService) cls.getAnnotation(YcService.class);
        YcRepository ycRepository= (YcRepository) cls.getAnnotation(YcRepository.class);
        YcConfiguration ycConfiguration= (YcConfiguration) cls.getAnnotation(YcConfiguration.class);
        if (ycConfiguration!=null){
            return cls.getName();
        }
        String beanid=null;
        if (ycComponent!=null){
            beanid=ycComponent.value();
        }else if (ycController!=null){
            beanid=ycComponent.value();
        }else if (ycService!=null){
            beanid=ycService.value();
        }else if (ycRepository!=null){
            beanid=ycRepository.value();
        }
        if (beanid==null||"".equalsIgnoreCase(beanid)){
            String typename=cls.getSimpleName();
            beanid=typename.substring(0,1).toLowerCase()+typename.substring(1);
        }
        return beanid;
    }


    @Override
    public Object getBean(String beanid) {

        YcBeanDefinition bd=this.beanDefinitionMap.get(beanid);
        if (bd==null){
            throw new RuntimeException("容器中没有加载此bean");
        }
        String scope=bd.getScope();
        if ("prototype".equalsIgnoreCase(scope)){
            Object obj=null;
            try {
                obj=Class.forName(bd.getClassInfo()).newInstance();
                return obj;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (this.beanMap.containsKey(beanid)){
            return this.beanMap.get(beanid);
        }
        if (bd.isLazy()){
            Object obj=null;
            try {
                obj=Class.forName(bd.getClassInfo()).newInstance();
                this.beanMap.put(beanid,obj);
                return obj;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return obj;
        }
        return null;
    }
}
