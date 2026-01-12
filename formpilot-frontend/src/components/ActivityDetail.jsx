import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { getActivity, getActivityDetail } from "../services/api";
import { Box, Card, CardContent, Divider, Typography } from "@mui/material";

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState([]);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id);
        const activityResponse = await getActivity(id);
        setActivity(activityResponse.data);
        setRecommendation(response.data);
        console.log("Activity: ", activityResponse.data);
        console.log("recommendations of the activity: ", response.data);
      } catch (error) {
        console.error("Error fetching activity detail:", error);
      }
    };

    fetchActivityDetail();
  }, [id]);

  if (!activity) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Activity Details
          </Typography>
          <Typography>Type: {activity.type}</Typography>
          <Typography>Duration: {activity.duration}</Typography>
          <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
          <Typography>
            Date: {new Date(activity.createdAt).toLocaleDateString()}
          </Typography>
        </CardContent>
      </Card>

      {recommendation && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              AI Recommendation
            </Typography>{" "}
            <Typography variant="h6">Analysis</Typography>
            <Typography paragraph>{recommendation.recommendation}</Typography>
            <Divider sx={{ my: 2 }} />
            <Typography variant="h6">Suggested Improvements</Typography>
            {recommendation?.improvements?.map((improvement, index) => (
              <Typography key={index} paragraph>
                {recommendation.improvements}
              </Typography>
            ))}
            <Divider sx={{ my: 2 }} />
            <Typography variant="h6"> Suggestions</Typography>
            {recommendation?.suggestions?.map((suggestion, index) => (
              <Typography key={index} paragraph>
                {suggestion}
              </Typography>
            ))}
            <Divider sx={{ my: 2 }} />
            <Typography variant="h6"> Safety Guidelines </Typography>
            {recommendation?.safety?.map((safety, index) => (
              <Typography key={index} paragraph>
                {safety}
              </Typography>
            ))}
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default ActivityDetail;
