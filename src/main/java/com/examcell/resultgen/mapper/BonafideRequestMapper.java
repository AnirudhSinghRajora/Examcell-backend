package com.examcell.resultgen.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.examcell.resultgen.dto.BonafideRequestDTO;
import com.examcell.resultgen.model.BonafideRequest;
import com.examcell.resultgen.util.SemesterUtil;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, UserMapper.class, SemesterUtil.class})
public interface BonafideRequestMapper {

    BonafideRequestMapper INSTANCE = Mappers.getMapper(BonafideRequestMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(target = "studentName", expression = "java(bonafideRequest.getStudent() != null ? bonafideRequest.getStudent().getFirstName() + \" \" + bonafideRequest.getStudent().getLastName() : null)")
    @Mapping(source = "student.rollNumber", target = "studentRollNo")
    @Mapping(source = "student.batchYear", target = "studentSemester", qualifiedByName = "mapBatchYearToSemester")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "approvedAt", target = "approvedAt")
    @Mapping(source = "purpose", target = "purpose")
    @Mapping(source = "customPurpose", target = "customPurpose")
    @Mapping(source = "additionalInfo", target = "additionalInfo")
    @Mapping(source = "rejectionReason", target = "rejectionReason")
    @Mapping(source = "certificateNumber", target = "certificateNumber")
    @Mapping(source = "certificatePath", target = "certificatePath")
    @Mapping(target = "approvedBy", expression = "java(bonafideRequest.getApprovedBy() != null ? bonafideRequest.getApprovedBy().getFirstName() + \" \" + bonafideRequest.getApprovedBy().getLastName() : null)")
    @Mapping(source = "status", target = "status")
    BonafideRequestDTO bonafideRequestToBonafideRequestDTO(BonafideRequest bonafideRequest);

    List<BonafideRequestDTO> bonafideRequestsToBonafideRequestDTOs(List<BonafideRequest> bonafideRequests);

    @Named("mapBatchYearToSemester")
    default String mapBatchYearToSemester(Integer batchYear) {
        if (batchYear == null) {
            return null;
        }
        return String.valueOf(SemesterUtil.calculateCurrentSemester(batchYear));
    }

    // Mapping back (for creating/updating requests from DTO to entity)
    // @Mapping(target = "student", ignore = true)
    // @Mapping(target = "processedBy", ignore = true)
    // @Mapping(target = "requestedAt", ignore = true)
    // @Mapping(target = "processedAt", ignore = true)
    // BonafideRequest bonafideRequestDTOToBonafideRequest(BonafideRequestDTO bonafideRequestDTO);
} 