package net.sourceforge.fenixedu.domain.contacts;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Country;

import org.apache.commons.lang.StringUtils;

public class PhysicalAddressData implements Serializable {

    private String address;
    private String areaCode;
    private String areaOfAreaCode;
    private String area;
    private String parishOfResidence;
    private String districtSubdivisionOfResidence;
    private String districtOfResidence;

    private Country countryOfResidence;

    public PhysicalAddressData() {
    }

    public PhysicalAddressData(final PhysicalAddress address) {
        setAddress(address.getAddress());
        setAreaCode(address.getAreaCode());
        setAreaOfAreaCode(address.getAreaOfAreaCode());
        setArea(address.getArea());
        setParishOfResidence(address.getParishOfResidence());
        setDistrictSubdivisionOfResidence(address.getDistrictSubdivisionOfResidence());
        setDistrictOfResidence(address.getDistrictOfResidence());
        setCountryOfResidence(address.getCountryOfResidence());
    }

    public PhysicalAddressData(final String address, final String areaCode, final String areaOfAreaCode, final String area) {
        this(address, areaCode, areaOfAreaCode, area, null, null, null, null);
    }

    public PhysicalAddressData(final String address, final String areaCode, final String areaOfAreaCode, final String area,
            final String parishOfResidence, final String districtSubdivisionOfResidence, final String districtOfResidence,
            final Country countryOfResidence) {

        setAddress(address);
        setAreaCode(areaCode);
        setAreaOfAreaCode(areaOfAreaCode);
        setArea(area);
        setParishOfResidence(parishOfResidence);
        setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
        setDistrictOfResidence(districtOfResidence);
        setCountryOfResidence(countryOfResidence);
    }

    public String getAddress() {
        return ifNullReturnEmpty(address);
    }

    public PhysicalAddressData setAddress(final String address) {
        this.address = address;
        return this;
    }

    public String getArea() {
        return ifNullReturnEmpty(area);
    }

    public PhysicalAddressData setArea(final String area) {
        this.area = area;
        return this;
    }

    public String getAreaCode() {
        return ifNullReturnEmpty(areaCode);
    }

    public PhysicalAddressData setAreaCode(final String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    private String ifNullReturnEmpty(final String value) {
        if (value == null) {
            return StringUtils.EMPTY;
        }
        return value;
    }

    public String getAreaOfAreaCode() {
        return ifNullReturnEmpty(areaOfAreaCode);
    }

    public PhysicalAddressData setAreaOfAreaCode(final String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
        return this;
    }

    public String getDistrictOfResidence() {
        return ifNullReturnEmpty(districtOfResidence);
    }

    public PhysicalAddressData setDistrictOfResidence(final String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
        return this;
    }

    public String getDistrictSubdivisionOfResidence() {
        return ifNullReturnEmpty(districtSubdivisionOfResidence);
    }

    public PhysicalAddressData setDistrictSubdivisionOfResidence(final String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
        return this;
    }

    public String getParishOfResidence() {
        return ifNullReturnEmpty(parishOfResidence);
    }

    public PhysicalAddressData setParishOfResidence(final String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
        return this;
    }

    private String getCountryOfResidenceName() {
        return getCountryOfResidence() == null ? StringUtils.EMPTY : getCountryOfResidence().getName();
    }

    public Country getCountryOfResidence() {
        return this.countryOfResidence;
    }

    public PhysicalAddressData setCountryOfResidence(final Country countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
        return this;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PhysicalAddressData) {
            final PhysicalAddressData data = (PhysicalAddressData) obj;
            return getAddress().equals(data.getAddress()) && getAreaCode().equals(data.getAreaCode())
                    && getAreaOfAreaCode().equals(data.getAreaOfAreaCode()) && getArea().equals(data.getArea())
                    && getParishOfResidence().equals(data.getParishOfResidence())
                    && getDistrictSubdivisionOfResidence().equals(data.getDistrictSubdivisionOfResidence())
                    && getDistrictOfResidence().equals(data.getDistrictOfResidence())
                    && getCountryOfResidenceName().equals(data.getCountryOfResidenceName());
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(address) && StringUtils.isEmpty(areaCode) && StringUtils.isEmpty(areaOfAreaCode)
                && StringUtils.isEmpty(area) && StringUtils.isEmpty(parishOfResidence)
                && StringUtils.isEmpty(districtSubdivisionOfResidence) && StringUtils.isEmpty(districtOfResidence)
                && countryOfResidence == null;
    }

}
