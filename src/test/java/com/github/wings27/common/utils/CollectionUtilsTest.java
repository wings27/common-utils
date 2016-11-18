package com.github.wings27.common.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.*;
import java.util.function.Function;

/**
 * Project common-utils
 * Created by wenqiushi on 2016-11-18 17:07.
 */
public class CollectionUtilsTest {

    @Rule
    public TestName name = new TestName();

    private List<Student> studentList = new ArrayList<Student>() {{
        add(new Student(1, "Alice", 18).chooseCourse("Ma").chooseCourse("Ph"));
        add(new Student(2, "Bob", 19).chooseCourse("Ph").chooseCourse("Bi").chooseCourse("Ma"));
        add(new Student(3, "Carol", 20).chooseCourse("Ma").chooseCourse("Ch").chooseCourse("Bi"));
        add(new Student(4, "Dave", 18).chooseCourse("Bi"));
        add(new Student(5, "Eve", 17).chooseCourse("Ma").chooseCourse("Ch"));
    }};

    @Test
    public void testUniqueIndex() throws Exception {
        Map<Integer, Student> studentMap = CollectionUtils.uniqueIndex(studentList, Student::getId);
        System.out.println(name.getMethodName() + ": " + studentMap);
    }

    @Test
    public void testGroupByToList() throws Exception {
        Map<Integer, List<Student>> groupByAge = CollectionUtils.groupByToList(studentList, Student::getAge);
        System.out.println(name.getMethodName() + ": " + groupByAge);
    }

    @Test
    public void testGroupByToSet() throws Exception {
        Map<Integer, Set<Student>> groupByAge = CollectionUtils.groupByToSet(studentList, Student::getAge);
        System.out.println(name.getMethodName() + ": " + groupByAge);
    }

    @Test
    public void testMultimapGroupBy() throws Exception {
        Multimap<Integer, Student> groupByAge = CollectionUtils.multimapGroupBy(studentList,
                Student::getAge, Function.identity(), HashMultimap::create);
        System.out.println(name.getMethodName() + ": " + groupByAge);

        Multimap<Integer, String> groupByAge1 = CollectionUtils.multimapGroupBy(studentList,
                Student::getAge, Student::getName, HashMultimap::create);
        System.out.println(name.getMethodName() + ": " + groupByAge1);
    }

    @Test
    public void testMultimapGroupByFlat() throws Exception {
        HashMultimap<Integer, String> groupByAge = CollectionUtils.multimapGroupByFlat(studentList,
                Student::getAge, Student::getCourses, HashMultimap::create);
        System.out.println(name.getMethodName() + ": " + groupByAge);
    }

    @Test
    public void testMapAndCollectToSet() throws Exception {

    }

    @Test
    public void testMapAndCollectToList() throws Exception {

    }

    @Test
    public void testMapAndCollectToMap() throws Exception {

    }

    private class Student {
        private int id;
        private String name;
        private int age;
        private List<String> courses = new ArrayList<>();

        public Student(int id, String name, int age) {
            this.setId(id);
            this.setName(name);
            this.setAge(age);
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return getId() == student.getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Student chooseCourse(String courseName) {
            this.courses.add(courseName);
            return this;
        }

        public List<String> getCourses() {
            return this.courses;
        }

    }

}