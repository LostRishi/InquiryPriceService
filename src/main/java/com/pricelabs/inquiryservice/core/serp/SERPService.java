package com.pricelabs.inquiryservice.core.serp;

import com.pricelabs.inquiryservice.core.dto.InquiryResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SERPService {


    public static List<InquiryResponse> getInquiriesPrice(int pageSize, String address) throws IOException, ParseException {
        try {
            List<InquiryResponse> inquiryResponseList = null;

            String response = getInquiries(pageSize, address);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);

            JSONObject data = (JSONObject) json.get("data");
            JSONObject results = (JSONObject) data.get("results");
            JSONObject listings = (JSONObject) results.get("listings");
            System.out.println(listings.size());
            for (int i = 0; i < listings.size(); i++) {
                InquiryResponse inquiryResponse = new InquiryResponse();
                JSONObject l = (JSONObject) listings.get(i);
                System.out.println((String) l.get("listingId"));
//            inquiryResponse.setListing_id((String) l.get("listingId"));

                inquiryResponseList.add(inquiryResponse);
            }
            return inquiryResponseList;
        } catch (IOException | ParseException e) {
            System.out.println("Exception in getInquiriesPrice function with message: " + e.getMessage());
            return null;
        }
    }


    public static String getInquiries(int pageSize, String address) throws IOException, ParseException {
        try {

            RestAssured.baseURI = "https://www.vrbo.com/serp/g";
            JSONObject jsonObject = getUpdatedJsonObject(pageSize, address);

            System.out.println(jsonObject);
            //TODO: convert jsonObject to String payload

            String payload = "{\n    \"operationName\": \"SearchRequestQuery\",\n    \"variables\": {\n        \"filterCounts\": true,\n        \"request\": {\n            \"paging\": {\n                \"page\": 1,\n                \"pageSize\": 50\n            },\n            \"filterVersion\": \"1\",\n            \"coreFilters\": {\n                \"adults\": 1,\n                \"maxBathrooms\": null,\n                \"maxBedrooms\": null,\n                \"maxNightlyPrice\": null,\n                \"maxTotalPrice\": null,\n                \"minBathrooms\": 0,\n                \"minBedrooms\": 0,\n                \"minNightlyPrice\": 0,\n                \"minTotalPrice\": null,\n                \"pets\": 0\n            },\n            \"filters\": [],\n            \"q\": \"chicago-illinois-united-states-of-america\"\n        },\n        \"optimizedBreadcrumb\": false,\n        \"vrbo_web_global_messaging_banner\": true\n    },\n    \"extensions\": {\n        \"isPageLoadSearch\": true\n    },\n    \"query\": \"query SearchRequestQuery($request: SearchResultRequest!, $filterCounts: Boolean!, $optimizedBreadcrumb: Boolean!, $vrbo_web_global_messaging_banner: Boolean!) {  results: search(request: $request) {    ...querySelectionSet    ...DestinationBreadcrumbsSearchResult    ...DestinationMessageSearchResult    ...FilterCountsSearchRequestResult    ...HitCollectionSearchResult    ...ADLSearchResult    ...MapSearchResult    ...ExpandedGroupsSearchResult    ...PagerSearchResult    ...SearchTermCarouselSearchResult    ...InternalToolsSearchResult    ...SEOMetaDataParamsSearchResult    ...GlobalInlineMessageSearchResult    ...GlobalBannerContainerSearchResult @include(if: $vrbo_web_global_messaging_banner)    __typename  }}fragment querySelectionSet on SearchResult {  id  typeaheadSuggestion {    uuid    term    name    __typename  }  geography {    lbsId    gaiaId    location {      latitude      longitude      __typename    }    isGeocoded    shouldShowMapCentralPin    __typename  }  propertyRedirectUrl  __typename}fragment DestinationBreadcrumbsSearchResult on SearchResult {  destination(optimizedBreadcrumb: $optimizedBreadcrumb) {    breadcrumbs {      name      url      __typename    }    __typename  }  __typename}fragment HitCollectionSearchResult on SearchResult {  page  pageSize  pageCount  queryUUID  percentBooked {    currentPercentBooked    __typename  }  listings {    ...HitListing    __typename  }  resultCount  pinnedListing {    headline    listing {      ...HitListing      __typename    }    __typename  }  __typename}fragment HitListing on Listing {  virtualTourBadge {    name    id    helpText    __typename  }  amenitiesBadges {    name    id    helpText    __typename  }  images {    altText    c6_uri    c9_uri    mab {      banditId      payloadId      campaignId      cached      arm {        level        imageUrl        categoryName        __typename      }      __typename    }    __typename  }  ...HitInfoListing  __typename}fragment HitInfoListing on Listing {  listingId  ...HitInfoDesktopListing  ...HitInfoMobileListing  ...PriceListing  __typename}fragment HitInfoDesktopListing on Listing {  detailPageUrl unitApiUrl  instantBookable  minStayRange {    minStayHigh    minStayLow    __typename  }  listingId  listingNumber  rankedBadges(rankingStrategy: SERP) {    id    helpText    name    __typename  }  propertyId  propertyMetadata {    headline    __typename  }  superlativesBadges: rankedBadges(rankingStrategy: SERP_SUPERLATIVES) {    id    helpText    name    __typename  }  unitMetadata {    unitName    __typename  }  webRatingBadges: rankedBadges(rankingStrategy: SRP_WEB_RATING) {    id    helpText    name    __typename  }  ...DetailsListing  ...GeoDistanceListing  ...RateSummary ...PriceListing  ...RatingListing  __typename}fragment DetailsListing on Listing {  bathrooms {    full    half    toiletOnly    __typename  }  bedrooms  propertyType  sleeps  petsAllowed  spaces {    spacesSummary {      area {        areaValue        __typename      }      bedCountDisplay      __typename    }    __typename  }  __typename}fragment GeoDistanceListing on Listing {  geoDistance {    text    relationType    __typename  }  __typename}  fragment RateSummary on Listing { rateSummary { beginDate  endDate rentNights } } fragment PriceListing on Listing {  priceSummary: priceSummary {  priceAccurate    ...PriceSummaryTravelerPriceSummary    __typename  }  priceSummarySecondary: priceSummary(summary: \\\"displayPriceSecondary\\\") {    ...PriceSummaryTravelerPriceSummary    __typename  }  priceLabel: priceSummary(summary: \\\"priceLabel\\\") {    priceTypeId    pricePeriodDescription    __typename  }  prices {    ...VrboTravelerPriceSummary    __typename  }  __typename}fragment PriceSummaryTravelerPriceSummary on TravelerPriceSummary {  priceTypeId  edapEventJson  formattedAmount  roundedFormattedAmount  pricePeriodDescription  __typename}fragment VrboTravelerPriceSummary on PriceSummary {  perNight {    amount    formattedAmount    roundedFormattedAmount    pricePeriodDescription    __typename  }  total {    amount    formattedAmount    roundedFormattedAmount    pricePeriodDescription    __typename  }  label  mainPrice  __typename}fragment RatingListing on Listing {  averageRating  reviewCount  __typename}fragment HitInfoMobileListing on Listing {  detailPageUrl  instantBookable  minStayRange {    minStayHigh    minStayLow    __typename  }  listingId  listingNumber  rankedBadges(rankingStrategy: SERP) {    id    helpText    name    __typename  }  propertyId  propertyMetadata {    headline    __typename  }  superlativesBadges: rankedBadges(rankingStrategy: SERP_SUPERLATIVES) {    id    helpText    name    __typename  }  unitMetadata {    unitName    __typename  }  webRatingBadges: rankedBadges(rankingStrategy: SRP_WEB_RATING) {    id    helpText    name    __typename  }  ...DetailsListing  ...GeoDistanceListing ...RateSummary ...PriceListing  ...RatingListing  __typename}fragment ExpandedGroupsSearchResult on SearchResult {  expandedGroups {    ...ExpandedGroupExpandedGroup    __typename  }  __typename}fragment ExpandedGroupExpandedGroup on ExpandedGroup {  listings {    ...HitListing    ...MapHitListing    __typename  }  mapViewport {    neLat    neLong    swLat    swLong    __typename  }  __typename}fragment MapHitListing on Listing {  ...HitListing  geoCode {    latitude    longitude    __typename  }  __typename}fragment FilterCountsSearchRequestResult on SearchResult {  id  resultCount  filterGroups {    groupInfo {      name      id      __typename    }    filters {      count @include(if: $filterCounts)      checked      filter {        id        name        refineByQueryArgument        description        __typename      }      __typename    }    __typename  }  __typename}fragment MapSearchResult on SearchResult {  mapViewport {    neLat    neLong    swLat    swLong    __typename  }  page  pageSize  listings {    ...MapHitListing    __typename  }  pinnedListing {    listing {      ...MapHitListing      __typename    }    __typename  }  __typename}fragment PagerSearchResult on SearchResult {  fromRecord  toRecord  pageSize  pageCount  page  resultCount  __typename}fragment DestinationMessageSearchResult on SearchResult {  destinationMessage(assetVersion: 4) {    iconTitleText {      title      message      icon      messageValueType      link {        linkText        linkHref        __typename      }      __typename    }    ...DestinationMessageDestinationMessage    __typename  }  __typename}fragment DestinationMessageDestinationMessage on DestinationMessage {  iconText {    message    icon    messageValueType    __typename  }  __typename}fragment ADLSearchResult on SearchResult {  parsedParams {    q    coreFilters {      adults      children      pets      minBedrooms      maxBedrooms      minBathrooms      maxBathrooms      minNightlyPrice      maxNightlyPrice      minSleeps      __typename    }    dates {      arrivalDate      departureDate      __typename    }    sort    __typename  }  page  pageSize  pageCount  resultCount  fromRecord  toRecord  pinnedListing {    listing {      listingId      __typename    }    __typename  }  listings {    listingId    __typename  }  filterGroups {    filters {      checked      filter {        groupId        id        __typename      }      __typename    }    __typename  }  geography {    lbsId    name    description    location {      latitude      longitude      __typename    }    primaryGeoType    breadcrumbs {      name      countryCode      location {        latitude        longitude        __typename      }      primaryGeoType      __typename    }    __typename  }  __typename}fragment SearchTermCarouselSearchResult on SearchResult {  discoveryXploreFeeds {    results {      id      title      items {        ... on SearchDiscoveryFeedItem {          type          imageHref          place {            uuid            name {              full              simple              __typename            }            __typename          }          __typename        }        __typename      }      __typename    }    __typename  }  typeaheadSuggestion {    name    __typename  }  __typename}fragment InternalToolsSearchResult on SearchResult {  internalTools {    searchServiceUrl    __typename  }  __typename}fragment SEOMetaDataParamsSearchResult on SearchResult {  page  resultCount  pageSize  geography {    name    lbsId    breadcrumbs {      name      __typename    }    __typename  }  __typename}fragment GlobalInlineMessageSearchResult on SearchResult {  globalMessages {    ...GlobalInlineAlertGlobalMessages    __typename  }  __typename}fragment GlobalInlineAlertGlobalMessages on GlobalMessages {  alert {    action {      link {        href        text {          value          __typename        }        __typename      }      __typename    }    body {      text {        value        __typename      }      link {        href        text {          value          __typename        }        __typename      }      __typename    }    id    severity    title {      value      __typename    }    __typename  }  __typename}fragment GlobalBannerContainerSearchResult on SearchResult {  globalMessages {    ...GlobalBannerGlobalMessages    __typename  }  __typename}fragment GlobalBannerGlobalMessages on GlobalMessages {  banner {    body {      text {        value        __typename      }      link {        href        text {          value          __typename        }        __typename      }      __typename    }    id    severity    title {      value      __typename    }    __typename  }  __typename}\"\n}";
            Response postResponse = RestAssured.given()
                    .header("Content-Type", "application/json")
                    .contentType(ContentType.JSON)
                    .when()
                    .body(payload)
                    .post("https://www.vrbo.com/serp/g");

            ResponseBody body = postResponse.getBody();

//            System.out.println("Response Body is: " + body.asString());
            return body.toString();
        } catch (IOException | ParseException e) {
            System.out.println("Exception in getInquiries function with message: " + e.getMessage());
            return null;
        }
    }

    private static JSONObject getUpdatedJsonObject(int pageSize, String address) throws IOException, ParseException {
        try {
            JSONParser jsonParser= new JSONParser();
            FileReader reader = new FileReader("src/main/resources/SDE_Code_Test.postman_collection.json");
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

//            TODO: Update pageSize and address with the two values in json object
//        System.out.println(jsonObject);
//        JSONObject getPage = (JSONObject) jsonObject.get("raw");
//        JSONObject variables = (JSONObject) getPage.get("variables");
//        JSONObject request = (JSONObject) variables.get("request");
//
//        JSONObject paging = (JSONObject) request.get("paging");
//        paging.put("pageSize",pageSize);
//
//        request.put("q",address);

            return jsonObject;
        } catch (IOException | ParseException e) {
            System.out.println("Exception in getUpdatedJsonObject function with message: " + e.getMessage());
            return null;
        }
    }
}
