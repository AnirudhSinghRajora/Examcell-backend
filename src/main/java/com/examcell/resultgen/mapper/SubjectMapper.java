package com.examcell.resultgen.mapper;

import com.examcell.resultgen.dto.SubjectDto;
import com.examcell.resultgen.model.Branch;
import com.examcell.resultgen.model.Course;
import com.examcell.resultgen.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "courseName", expression = "java(subject.getCourse().name())")
    @Mapping(source = "branches", target = "branchNames", qualifiedByName = "mapBranchesToStrings")
    SubjectDto subjectToSubjectDto(Subject subject);

    List<SubjectDto> subjectsToSubjectDtos(List<Subject> subjects);

    @Mapping(target = "course", source = "courseName", qualifiedByName = "mapStringToCourse")
    @Mapping(source = "branchNames", target = "branches", qualifiedByName = "mapStringsToBranches")
    Subject subjectDtoToSubject(SubjectDto subjectDto);

    @Named("mapBranchesToStrings")
    default List<String> mapBranchesToStrings(Set<Branch> branches) {
        if (branches == null) {
            return null;
        }
        return branches.stream().map(Enum::name).collect(Collectors.toList());
    }
    @Named("mapStringsToBranches")
    default Set<Branch> mapStringsToBranches(List<String> branchNames) {
        if (branchNames == null) {
            return new HashSet<>();
        }
        return branchNames.stream().map(Branch::valueOf).collect(Collectors.toSet());
    }

    @Named("mapStringToCourse")
    default Course mapStringToCourse(String courseName) {
        if (courseName == null) {
            return null;
        }
        return Course.valueOf(courseName);
    }
}